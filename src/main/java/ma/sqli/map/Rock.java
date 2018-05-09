package ma.sqli.map;

abstract class Rock implements MapSlot
{
  private boolean exploded = false;
  
  abstract char drawWhenExploded();

  @Override
  public final char draw()
  {
    return exploded ? drawWhenExploded() : 'X';
  }
  
  final void explode()
  {
    exploded = true;
  }
  
  final boolean isExploded()
  {
    return exploded;
  }
}
