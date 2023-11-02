package hr.algebra.carcassonnegame2.model.gameobjects;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;

public final class TileImpl extends Tile {

    private TileElementValue[][] tileGrid;
    private Position followerPosition;
    private int followerPlayer;
    private Position positionInGameBoard;
    private boolean citiesAreConnected=false;

    public TileImpl(TileElementValue[][] tileGrid) {
        this.tileGrid = tileGrid;
        this.removeFollower();
        initializeCitiesAreConnected();
    }

    public TileImpl(TileElementValue[][] representation, Position positionInGameBoard) {
        this(representation);
        this.positionInGameBoard=positionInGameBoard;
    }

    @Override
    public Tile catchTile() {
        return new TileImpl(tileGrid);
    }

    @Override
    public void removeFollower() {
        this.followerPlayer = -1;
        this.followerPosition=null;
    }

    @Override
    public boolean canPutTile(Tile otherTile, RelativePositionGrid positionFromOtherTile) {
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
        TileElementValue[][] newTileGrid = new TileElementValue[NUM_ROWS_TILE][NUM_COLS_TILE];

        for (int col = 0; col < NUM_COLS_TILE; col++) {
            for (int row = 0; row < NUM_ROWS_TILE; row++) {
                newTileGrid[col][row] = tileGrid[row][NUM_COLS_TILE - 1 - col];
            }
        }

        this.tileGrid = newTileGrid;
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
    public TileElementValue getValuePosition(Position point){
        return this.tileGrid[point.getCol()][point.getRow()];
    }
    @Override
    public boolean canPutFollower(Position position) {
        if (!isFollowerInPosition(position)) {
            TileElementValue positionValue = getValuePosition(position);

            if (positionValue.isPath()) {
                return checkPutFollowerInPath(position);
            }

            if (positionValue.isCity()) {
                return checkPutFollowerInCity(position);
            }
            return true;//if isMonastery
        }
        return false;
    }
    private boolean checkPutFollowerInPath(Position position) {
        if(getValuePosition(getCenterPosition()).isIntersection()){
             if( !isFollowerInPathSection(position)){
                 if(position.getRow()==NUM_ROWS_TILE/2){
                     if(position.getCol()<NUM_COLS_TILE/2)
                         return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.LEFT), getRightPosition());
                     else
                         return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.RIGHT), getLeftPosition());
                 }else {
                     if (position.getRow() < NUM_ROWS_TILE / 2)
                         return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.TOP), getBottomPosition());
                     else
                         return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.BOTTOM), getTopPosition());
                 }
             }
             return false;
        }else{
            if(!isFollowerInPosition(getCenterPosition()) && isFollowerInPath()) {
                return callGameToCheckOtherTile(TileElementValue.PATH);
            }
            return false;
        }
    }

    private boolean isFollowerInPath() {
        return (!getValuePosition(getTopPosition()).isPath() || !isFollowerInPathSection(getTopPosition()))
                && (!getValuePosition(getRightPosition()).isPath() || !isFollowerInPathSection(getRightPosition()))
                && (!getValuePosition(getLeftPosition()).isPath() || !isFollowerInPathSection(getLeftPosition()))
                && (!getValuePosition(getBottomPosition()).isPath() || !isFollowerInPathSection(getBottomPosition()));
    }

    private boolean isFollowerInPathSection(Position position){
        boolean returnValue=isFollowerInPosition(position) || isFollowerInPosition(getCenterPosition());
        if(isPositionInsideTile(position.getPositionInTop()) && getValuePosition(position.getPositionInTop()).isPath() && !returnValue){
            returnValue = isFollowerInPosition(position.getPositionInTop());
        }
        if(isPositionInsideTile(position.getPositionInBottom()) && getValuePosition(position.getPositionInBottom()).isPath() && !returnValue){
            returnValue = isFollowerInPosition(position.getPositionInBottom());
        }
        if(isPositionInsideTile(position.getPositionInLeft()) && getValuePosition(position.getPositionInLeft()).isPath()&& !returnValue){
            returnValue = isFollowerInPosition(position.getPositionInLeft());
        }
        if(isPositionInsideTile(position.getPositionInRight()) && getValuePosition(position.getPositionInRight()).isPath()&& !returnValue) {
            returnValue = isFollowerInPosition(position.getPositionInRight());
        }
        return returnValue;
    }

    private boolean checkPutFollowerInCity(Position position) {
        if(getValuePosition(getCenterPosition()).isCity()){
            return (followerPosition == null || !getValuePosition(followerPosition).isCity())
                    && callGameToCheckOtherTile(TileElementValue.CITY);
        }else{
            if (citiesAreConnected) {
                return (followerPosition == null || !getValuePosition(followerPosition).isCity())
                        && callGameToCheckOtherTile(TileElementValue.CITY);
            } else {
                if(isFollowerInCity(position)){
                    return false;
                }
                if (position.getCol() == 0) {
                    return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.LEFT), getRightPosition());
                } else if (position.getCol() == NUM_COLS_TILE - 1) {
                    return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.RIGHT), getLeftPosition());
                } else if (position.getRow() == 0) {
                    return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.TOP), getBottomPosition());
                }
                return Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.BOTTOM),getTopPosition());
            }

        }
    }

    private boolean isFollowerInCity(Position position) {
        if(isPositionInsideTile(position.getPositionInTop()) && getValuePosition(position.getPositionInTop()).isCity() && isFollowerInPosition(position.getPositionInTop())) return true;
        if(isPositionInsideTile(position.getPositionInBottom()) && getValuePosition(position.getPositionInBottom()).isCity() && isFollowerInPosition(position.getPositionInBottom())) return true;
        if(isPositionInsideTile(position.getPositionInLeft()) && getValuePosition(position.getPositionInLeft()).isCity() && isFollowerInPosition(position.getPositionInLeft())) return true;
        if(isPositionInsideTile(position.getPositionInRight()) && getValuePosition(position.getPositionInRight()).isCity() && isFollowerInPosition(position.getPositionInRight())) return true;
        return isFollowerInPosition(position);
    }

    private boolean callGameToCheckOtherTile(TileElementValue value){
        boolean returnValue = true;
        if(getValuePosition(getTopPosition()).areTileElementsCompatible(value)){
            returnValue = Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.TOP), getBottomPosition());
        }
        if(getValuePosition(getBottomPosition()).areTileElementsCompatible(value) && returnValue){
            returnValue = Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.BOTTOM), getTopPosition());
        }
        if(getValuePosition(getRightPosition()).areTileElementsCompatible(value) && returnValue){
            returnValue = Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.RIGHT), getLeftPosition());
        }
        if(getValuePosition(getLeftPosition()).areTileElementsCompatible(value) && returnValue){
            returnValue = Game.INSTANCE.checkPositionInTileForFollower(this.positionInGameBoard.castRelativePositionToPoint(RelativePositionGrid.LEFT), getRightPosition());
        }
        return returnValue;
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
    public void setFollower(int player, Position position) {
        this.followerPlayer = player;
        this.followerPosition = position;
    }

    @Override
    public boolean hasIntersection() {
        return getValuePosition(getCenterPosition()).isIntersection();
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
    public int getPlayerFollower() {
        return followerPlayer;
    }

    @Override
    public int countPathsForClosingPath(Position position) {
        if(this.pathEnd()) {
            setTileWithFollowerInPath(position);
            return 1;
        }else{
            int counter = 0;
            setTileWithFollowerInPath(position);
            if(!position.equals(getLeftPosition()) || position.equals(getCenterPosition())) counter += countPathForClosingPathAux(getLeftPosition().getPositionInRight(), positionInGameBoard.getPositionInLeft(), getRightPosition());
            if(!position.equals(getRightPosition()) || position.equals(getCenterPosition())) counter += countPathForClosingPathAux(getRightPosition().getPositionInLeft(), positionInGameBoard.getPositionInRight(), getLeftPosition());
            if(!position.equals(getTopPosition()) || position.equals(getCenterPosition())) counter += countPathForClosingPathAux(getTopPosition().getPositionInBottom(), positionInGameBoard.getPositionInTop(), getBottomPosition());
            if (!position.equals(getBottomPosition()) || position.equals(getCenterPosition())) counter += countPathForClosingPathAux(getBottomPosition().getPositionInTop(), positionInGameBoard.getPositionInBottom(), getTopPosition());
            return counter;

        }
    }

    @Override
    public boolean pathEnd() {
        int counter = 0;
        counter+= getValuePosition(getRightPosition()).isPath()?1:0;
        counter+= getValuePosition(getLeftPosition()).isPath()?1:0;
        counter+= getValuePosition(getTopPosition()).isPath()?1:0;
        counter+= getValuePosition(getBottomPosition()).isPath()?1:0;
        return getValuePosition(getCenterPosition()).isIntersection()
                || counter == 1;
    }

    private int countPathForClosingPathAux(Position position, Position positionOtherTileGB, Position positionInOtherTile){
        if(getValuePosition(position).isPath()){
            setTileWithFollowerInPath(position);
            return Game.INSTANCE.countPathForClosingPath(positionOtherTileGB, positionInOtherTile);
        }
        return 0;
    }
    @Override
    public void setTileWithFollowerInPath(Position position){
        if(isFollowerInPathSection(position)){
            Game.INSTANCE.setTileWithFollowerInCount(this);
        }
    }

    @Override
    public int countCitiesForClosingCities(Position position) {
        if (followerPosition != null && getValuePosition(followerPosition).isCity()) {
            setTileWithFollowerInCity();
        }
        return getAddingPointForThisCity(position) + (citiesAreConnected ? getCountCityFromOtherTiles():0);
    }

    @Override
    public Position getNextCityValue(Position position) {
        Position pos = position;
        do {
            pos= castRelativeToPositionInTile(castPositionInTileToRelative(pos).getNext());
        }
        while(!getValuePosition(pos).isCity());
        return pos;
    }

    @Override
    public void prepareForClosingPath(Position position) {
        if(isFollowerInPathSection(position)){
            Game.INSTANCE.setTileWithFollowerInCount(this);
        }
    }

    private void setTileWithFollowerInCity() {
        Game.INSTANCE.setTileWithFollowerInCount(this);
    }

    private int getCountCityFromOtherTiles() {
        int count = getCountCityFromOtherTilesAux(getLeftPosition());
        count+= getCountCityFromOtherTilesAux(getRightPosition());
        count+= getCountCityFromOtherTilesAux(getBottomPosition());
        return count + getCountCityFromOtherTilesAux(getTopPosition());
    }

    private int getCountCityFromOtherTilesAux(Position position){ //Ultimo toque ha sido cambiar getCountOfCitiesFromOneTile(getNextPositionInGameBoard(position), position)
        return getValuePosition(position).isCity() ? getCountOfCitiesFromOneTile(getNextPositionInGameBoard(position), getOtherPosition(position)) : 0;
    }

    private int getCountOfCitiesFromOneTile(Position positionInGameBoardForOtherTile, Position positionInsideOtherTile) {
        return Game.INSTANCE.countCitiesForTile(positionInGameBoardForOtherTile, positionInsideOtherTile);
    }


    public int getAddingPointForThisCity(Position position){
        if(citiesAreConnected){
            if(getValuePosition(getLeftPosition()).hasShield() ||
                    getValuePosition(getRightPosition()).hasShield() ||
                    getValuePosition(getTopPosition()).hasShield() ||
                    getValuePosition(getBottomPosition()).hasShield())
                return 2;
        }
        if(getValuePosition(position).hasShield()){
            return 2;
        }
        return 0;
    }

    @Override
    public void setIfFollowerInCity(Position position) {
        if(isFollowerInCity(position)){
            setTileWithFollowerInCity();
        }
    }

    @Override
    public Position getNextPositionInGameBoard(Position positionInTile) {
        RelativePositionGrid relative = Tile.castPositionInTileToRelative(positionInTile);
        return this.positionInGameBoard.castRelativePositionToPoint(relative);
    }

    @Override
    public boolean hasPath() {
        return getValuePosition(getCenterPosition()).isPath();
    }

    private boolean isPositionInsideTile(Position position){
        int col = position.getCol(), row = position.getRow();
        return col>=0 && col<NUM_COLS_TILE && row>=0 && row<NUM_ROWS_TILE;
    }

    private void initializeCitiesAreConnected(){
        if(getValuePosition(getCenterPosition()).isCity()){
            citiesAreConnected = true;
        }else if((getValuePosition(getLeftPosition()).isCity() || getValuePosition(getRightPosition()).isCity())
                && (getValuePosition(getTopPosition()).isCity() || getValuePosition(getBottomPosition()).isCity())){
            citiesAreConnected = isOneCornerACity();
        }
    }

    public boolean areCitiesConnected(){
        return citiesAreConnected;
    }

    private boolean isOneCornerACity() {
        return getValuePosition(new Position(0, 0)).isCity() ||
        getValuePosition(new Position(NUM_COLS_TILE-1, 0)).isCity() ||
        getValuePosition(new Position(0, NUM_ROWS_TILE-1)).isCity() ||
        getValuePosition(new Position(NUM_COLS_TILE-1, NUM_ROWS_TILE-1)).isCity();

    }
}
