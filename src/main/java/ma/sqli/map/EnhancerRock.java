package ma.sqli.map;

final class EnhancerRock extends Rock
{
  private final int enhanceRange;
  
  EnhancerRock(final int enhanceRange)
  {
    this.enhanceRange = enhanceRange;
  }
  
  @Override
  char drawWhenExploded()
  {
    return String.valueOf(enhanceRange).charAt(0);
  }

  int getEnhanceRange()
  {
    return enhanceRange;
  }

}
