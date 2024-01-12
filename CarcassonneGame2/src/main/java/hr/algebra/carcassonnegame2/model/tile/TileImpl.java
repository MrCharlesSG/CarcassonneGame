package hr.algebra.carcassonnegame2.model.tile;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.utils.GridUtils;
import hr.algebra.carcassonnegame2.utils.TileUtils;
import javafx.geometry.Pos;

public final class TileImpl extends Tile {
    private Position followerPosition;
    private Player followerPlayer;
    private Position positionInGameBoard;
    private boolean citiesAreConnected=false;
    private final TileManagement tilePathManager;
    private final TileManagement tileCityManager;

    public TileImpl(TileElementValue[][] tileGrid, boolean needsToManageGame) {
        this.tileGrid = tileGrid;
        this.removeFollower();
        if(needsToManageGame) {
            tilePathManager = TileManagement.getInstance(TileElementValue.PATH, this);
            tileCityManager = TileManagement.getInstance(TileElementValue.CITY, this);
        }else{
            tilePathManager=null;
            tileCityManager=null;
        }
    }

    public TileImpl(TileElementValue[][] representation, Position positionInGameBoard) {
        this(representation, true);
        this.positionInGameBoard=positionInGameBoard;
    }

    public TileImpl(Tile tile){
        this(tile.getRepresentation(), tile.getPositionInGameBoard());
    }

    public TileImpl(TileElementValue[][] tileGrid, Position positionInGameBoard, Player player, Position followerPosition) {
        this.tileGrid = tileGrid;
        this.positionInGameBoard=positionInGameBoard;
        this.tileCityManager=null;
        this.tilePathManager=null;
        this.followerPlayer=player;
        this.followerPosition = followerPosition;
    }

    @Override
    public Tile catchTile() {
        return new TileImpl(this);
    }

    @Override
    public void removeFollower() {
        this.followerPlayer = null;
        this.followerPosition=null;
    }

    @Override
    public boolean canPutTile(Tile otherTile, RelativePositionGrid positionFromOtherTile) {
        if(otherTile==null) return false;
        if(positionFromOtherTile == RelativePositionGrid.RIGHT){
            return getValuePosition(getLeftPosition()).areTileElementsCompatible(otherTile.getValuePosition(getRightPosition()));
        }
        else if(positionFromOtherTile == RelativePositionGrid.LEFT){
            return getValuePosition(getRightPosition()).areTileElementsCompatible(otherTile.getValuePosition(getLeftPosition()));
        }
        else if(positionFromOtherTile == RelativePositionGrid.BOTTOM){
            return getValuePosition(getTopPosition()).areTileElementsCompatible(otherTile.getValuePosition(getBottomPosition()));
        }
        else {
            return getValuePosition(getBottomPosition()).areTileElementsCompatible(otherTile.getValuePosition(getTopPosition()));
        }
    }

    @Override
    public void rotateTile() {
        tileGrid=GridUtils.rotateGrid(tileGrid, new TileElementValue[NUM_COLS_TILE][NUM_ROWS_TILE]);
    }

    @Override
    public TileElementValue[][] getRepresentation() {
        return this.tileGrid;
    }

    @Override
    public boolean hasFollower() {
        return followerPosition != null;
    }

    @Override
    public Position getFollowerPosition() {
        return followerPosition;
    }

    @Override
    public TileElementValue getValuePosition(Position position){
        return GridUtils.getValueOfPosition(tileGrid, position);
    }
    @Override
    public boolean canPutFollower(Position position) {
        if (!isFollowerInPosition(position)) {
            TileElementValue positionValue = getValuePosition(position);
            if (positionValue.isPath())
                return tilePathManager.checkPutFollower(position);
            if (positionValue.isCity())
                return tileCityManager.checkPutFollower(position);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFollowerInPosition(Position position) {
        return followerPosition!=null && followerPosition.equals(position);
    }

    @Override
    public boolean isValidPositionForFollower(Position position) {
        if(getValuePosition(position).isCity() && !getValuePosition(getCenterPosition()).isCity()){
            int col = position.getCol(), row = position.getRow();
            return col==0 || col == NUM_COLS_TILE-1 || row==0 || row==NUM_ROWS_TILE-1;
        }
        return  !getValuePosition(position).isFarm() && !getValuePosition(position).isIntersection(); //|| !isCorner(position);
    }

    @Override
    public void setPositionInGameBoard(Position position) {
        this.positionInGameBoard=position;
    }

    @Override
    public Position getPositionInGameBoard() {
        return positionInGameBoard;
    }

    @Override
    public void setFollower(Player player, Position position) {
        this.followerPlayer = player;
        this.followerPosition = position;
    }

    @Override
    public boolean hasCity() {
        return getValuePosition(getRightPosition()).isCity()
                || getValuePosition(getLeftPosition()).isCity()
                || getValuePosition(getTopPosition()).isCity()
                || getValuePosition(getBottomPosition()).isCity() ;
    }

    @Override
    public boolean hasMonasteryAndFollower() {
        return getValuePosition(getCenterPosition()).isMonastery() && isFollowerInPosition(getCenterPosition());
    }

    @Override
    public Player getPlayerFollower() {
        return followerPlayer;
    }

    @Override
    public int countPathsForClosingPath(Position position) {
        return tilePathManager.countPathsForClosingPath(position);
    }

    @Override
    public boolean pathEnd() {
        return tilePathManager.hasPathAnEnd();
    }

    @Override
    public int countCitiesForClosingCities(Position position) {
        return tileCityManager.countCitiesForClosingCities(position);
    }

    @Override
    public Position getNextCityValue(Position position) {
        return tileCityManager.getNextCityValue(position);
    }

    @Override
    public void prepareForClosingPath(Position position) {
        tilePathManager.prepareForClosingPath(position);
    }


    @Override
    public void setIfFollowerInCity(Position position) {
        tileCityManager.setIfFollowerInCity(position);
    }

    @Override
    public Position getNextPositionInGameBoard(Position positionInTile) {
        RelativePositionGrid relative = TileUtils.castPositionInTileToRelative(positionInTile);
        return this.positionInGameBoard.castRelativePositionToPoint(relative);
    }

    @Override
    public boolean hasPath() {
        return getValuePosition(getCenterPosition()).isPath();
    }

    @Override
    protected void setCitiesConnected(boolean citiesConnected) {
        this.citiesAreConnected = citiesConnected;
    }

    @Override
    public boolean areCitiesConnected(){
        return citiesAreConnected;
    }

    @Override
    public int getAddingPointForThisCity(Position position) {
        return tileCityManager.getAddingPointForThisCity(position);
    }
}
