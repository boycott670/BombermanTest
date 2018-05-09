package ma.sqli.map.bomberman;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ma.sqli.map.Map;
import ma.sqli.map.MapSlot;

public final class Bomberman implements MapSlot
{
  private final String name;
  private boolean isDead = false;
  
  private Map map;
  private List<Integer> headingDirections;
  private int headingDirection;
  private int headingExtent = 1;
  
  private boolean bombPreviousPosition = false;

  public Bomberman(String name)
  {
    this.name = name;
  }

  @Override
  public char draw()
  {
    return isDead ? 'D' : Character.toLowerCase(name.charAt(0));
  }
  
  public void startIn(final Map map)
  {
    (this.map = map).setBomberman(this);
    
    headingDirections = Collections.unmodifiableList(Arrays.stream(new int[]
    { 1, this.map.size(), -1, -this.map.size() })
        .boxed()
        .collect(Collectors.toList()));
    
    headingDirection = headingDirections.get(0);
  }
  
  public Bomberman forward()
  {
    map.moveBomberman(headingDirection * headingExtent);
    
    if (bombPreviousPosition)
    {
      map.setBomb();
      bombPreviousPosition = false;
    }
    
    return this;
  }
  
  public Bomberman right()
  {
    headingDirection = headingDirections.get(headingDirections.indexOf(headingDirection) + 1 % headingDirections.size());
    return this;
  }
  
  public Bomberman left()
  {
    headingDirection = headingDirections.get(Optional.of(headingDirections.indexOf(headingDirection) - 1).filter(nextHeadingDirectionIndex -> nextHeadingDirectionIndex >= 0).orElse(headingDirections.size() - 1));
    return this;
  }
  
  public Bomberman bomb()
  {
    bombPreviousPosition = true;
    return this;
  }
  
  public void die()
  {
    isDead = true;
  }

  public void setHeadingExtent(int headingExtent)
  {
    this.headingExtent = headingExtent;
  }
}
