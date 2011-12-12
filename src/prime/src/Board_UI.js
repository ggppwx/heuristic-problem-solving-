/**
 * @author roy
 */

goog.provide('hps.prime.BoardUI');

goog.require('hps.prime.NextTurnEvent');
goog.require('hps.prime.GameOverEvent');

goog.require('hps.prime.BoardFactory');

goog.require('goog.ui.Palette');
goog.require('goog.ui.Prompt');
goog.require('goog.dom.classes');
goog.require('goog.events.EventTarget');


var WIDTH_SIZE = 3;
var LENGTH_SIZE = 3;
  
hps.prime.BoardUI = function(filledCell, playerList,node){
    goog.base(this);
    this._isGameOver = false;
    this._board = hps.prime.BoardFactory.create(filledCell);
    this._players = playerList;

    var self = this;
    var items = [];
    for (var y = 0; y < LENGTH_SIZE; y++) {
	for (var x = 0; x < WIDTH_SIZE; x++) {
	    var cell = this._board.get(x,y);
	    var cellDisp = (cell === -1) ? '' : cell;
	    items.push(goog.dom.createTextNode(cellDisp));
	}
    }
    this._boardPalette = new goog.ui.Palette(items);
    this._boardPalette.setSize(WIDTH_SIZE);
    this._boardPalette.render(node);

    goog.dom.classes.add(this._boardPalette.getElement(),'simple-palette');

    goog.events.listen(this._boardPalette, goog.ui.Component.EventType.ACTION,
		       function(e) { self._selectCell(e.target); });

    this._refreshDisplay();
};
goog.inherits(hps.prime.BoardUI, goog.events.EventTarget);


hps.prime.BoardUI.prototype._selectCell = function(palette){
    var idx = palette.getSelectedIndex();
    var x = hps.prime.BoardUI._getX(idx);
    var y = hps.prime.BoardUI._getY(idx);
    var self = this;
    if(!this._isGameOver && this._board.get(x,y) === -1){
	var handler = function(response){ // handler
	    if(response != null){
		var n = parseInt(response);

		self._board.set(x,y,n);

		// get score 
		var addScore = self._board.getAddScore(x,y,n);
		// alert(addScore);
		// send next round event or game over event 
		if(self._board.moveNum() == 9){
		    // send player:addscore game over 
		    self.dispatchEvent(new hps.prime.NextTurnEvent(
			self._players.getCurrentPlayer(),addScore));
		    self._gameOver();
		}else{
		    // send player:addscore event to main ui 
		    self.dispatchEvent(new hps.prime.NextTurnEvent(
			self._players.getCurrentPlayer(),addScore));
		    if(self._board.moveNum() < 8){ // not reach move 8 
			self._players.next();
		    }

		}
		
		// self._refreshDisplay();
		goog.dom.setTextContent(palette.getSelectedItem(), n);
	    }
	};
	
	// pop up a promt window
	prompt = new goog.ui.Prompt('','Set the value',handler);

	prompt.setValidationFunction(function(input){
		var num = parseInt(input);
		return (num >=0 && num <=9);
	    });
	prompt.setVisible(true);
    }
};

hps.prime.BoardUI.prototype._gameOver = function(){
    this._isGameOver = true;
    this.dispatchEvent(new hps.prime.GameOverEvent());
};

hps.prime.BoardUI._getX = function(idx){
    return idx % WIDTH_SIZE;
};

hps.prime.BoardUI._getY = function(idx){
    return Math.floor(idx/WIDTH_SIZE);
};

hps.prime.BoardUI.prototype._refreshDisplay = function(){
    var x = 0;
    var y ;
    var cell;
    var origSelIdx;
    this._boardPalette.setEnabled(false);
    origSelIdx = this._boardPalette.getSelectedIndex();
    

    this._boardPalette.setEnabled(true);

};




hps.prime.BoardUI.createEmptyBoard = function(node){
    var items = [];
    for (var y = 0; y < LENGTH_SIZE; y++) {
	for (var x = 0; x < WIDTH_SIZE; x++) {
	    items.push(goog.dom.createTextNode(''));
	}
    }
    var boardPalette = new goog.ui.Palette(items);
    boardPalette.setSize(WIDTH_SIZE);
    
    boardPalette.render(node);
    // class is the key
    goog.dom.classes.add(boardPalette.getElement(),'simple-palette'); 
    

};




function sayHi() {
    var newHeader = goog.dom.createDom('h1', {'style': 'background-color:#EEE'},
    'Hello world!');
    goog.dom.appendChild(document.body, newHeader);

    var node = goog.dom.createDom('div',{'id':'prime-board'});
    goog.dom.appendChild(document.body, node);
    
    b = new hps.prime.BoardUI('',[],node);
    // hps.prime.BoardUI.renderEmptyBoard(node);
}

function test_Board_UI(){
    var node = goog.dom.createDom('div',{'id':'prime-board'});
    goog.dom.appendChild(document.body, node);
    p = new hps.prime.PlayerList([1,2]);
    b = new hps.prime.BoardUI('',p,node);
}