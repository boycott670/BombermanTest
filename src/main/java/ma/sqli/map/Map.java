package ma.sqli.map;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ma.sqli.map.bomberman.Bomberman;

public final class Map
{
  private final MapSlot[] slots;
  private final int size;
  
  private Bomberman bomberman;
  private int bombermanPosition;
  private int previousBombermanPosition;
  
  private int explosionExtent = 1;

  Map(MapSlot[] slots)
  {
    this.slots = slots;
    size = Double.valueOf(Math.sqrt(slots.length)).intValue();
  }
  
  public String draw()
  {
    return IntStream.range(0, size)
        .mapToObj(row -> IntStream.range(size * row, size * row + size)
            .mapToObj(index -> slots[index])
            .map(MapSlot::draw)
            .map(String::valueOf)
            .collect(Collectors.joining()))
        .collect(Collectors.joining("\n"));
  }
  
  public void setBomberman(final Bomberman bomberman)
  {
    slots[bombermanPosition = 0] = (this.bomberman = bomberman);
  }
  
  public void moveBomberman(final int headingDirection)
  {
    this.previousBombermanPosition = bombermanPosition;
    
    final int nextBombermanPosition = bombermanPosition + headingDirection;
    
    if (slots[nextBombermanPosition] instanceof StarRock && ((Rock)slots[nextBombermanPosition]).isExploded())
    {
      bomberman.setHeadingExtent(2);
    }
    else if (slots[nextBombermanPosition] instanceof EnhancerRock)
    {
      explosionExtent = ((EnhancerRock)slots[nextBombermanPosition]).getEnhanceRange();
    }
    
    slots[bombermanPosition = nextBombermanPosition] = bomberman;
    
    slots[previousBombermanPosition] = new EmptyMapSlot();
  }
  
  public int size()
  {
    return size;
  }
  
  public void setBomb()
  {
    slots[previousBombermanPosition] = new Bomb();
  }
  
  public void trigger()
  {
    final Function<Integer, int[]> getAdjacentSlots = position -> IntStream.rangeClosed(1, explosionExtent)
        .flatMap(extent -> IntStream
            .of(position - (position % size == 0 ? size : 1) * extent, position + (position % size == size - 1 ? size : 1) * extent,
                position - size * extent, position + size * extent))
        .distinct()
        .filter(adjacentPosition -> adjacentPosition >= 0 && adjacentPosition < slots.length)
        .toArray();

    final int[] adjacentSlots = IntStream.range(0, slots.length)
        .filter(position -> Bomb.class.isInstance(slots[position]))
        .peek(position -> slots[position] = new EmptyMapSlot())
        .boxed()
        .map(getAdjacentSlots)
        .flatMapToInt(Arrays::stream)
        .toArray();

    Arrays.stream(adjacentSlots)
        .mapToObj(position -> slots[position])
        .forEach(slot ->
        {
          if (slot instanceof Bomberman)
          {
            ((Bomberman) slot).die();
          } else if (slot instanceof Rock)
          {
            ((Rock) slot).explode();
          }
        });
  }
}
