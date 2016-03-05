package environment.ship.tile;

public interface Tileable {
	public void addTile(Tile tile);
	public void removeTile(Tile tile);
	public void reputTile(Tile tile);
	public void clearTiles();
	public void adjustGridSize(int xSize, int ySize);
}
