package ma.sqli.map;

import java.util.function.Supplier;
import java.util.stream.Stream;

public final class MapBuilder
{
  private final int mapSize;
  private final MapSlot[] mapSlots;

  public MapBuilder(int mapSize)
  {
    this.mapSize = mapSize;
    mapSlots = Stream.generate(EmptyMapSlot::new).limit(this.mapSize * this.mapSize).toArray(MapSlot[]::new);
  }
  
  private void addRock(final int line, final int column, final Supplier<? extends Rock> rockSupplier)
  {
    mapSlots[mapSize * line + column] = rockSupplier.get();
  }
  
  public MapBuilder addRock(final int line, final int column)
  {
    addRock(line, column, BasicRock::new);
    return this;
  }
  
  public MapBuilder addStarRock(final int line, final int column)
  {
    addRock(line, column, StarRock::new);
    return this;
  }
  
  public MapBuilder addEnhancerRock(final int line, final int column, final int enhanceRange)
  {
    addRock(line, column, () -> new EnhancerRock(enhanceRange));
    return this;
  }
  
  public Map build()
  {
    return new Map(mapSlots);
  }
}
