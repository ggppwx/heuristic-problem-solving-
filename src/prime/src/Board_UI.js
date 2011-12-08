/**
 * @author roy
 */

goog.provide('hps.prime.BoardUI');


goog.require('hps.prime.BoardFactory');

goog.require('goog.dom');
goog.require('goog.ui.Palette');
goog.require('goog.ui.Prompt');
goog.require('goog.events.EventTarget');


var WIDTH_SIZE = 3;
var LENGTH_SIZE = 3;

// goog.inherits(hps.prime.BoardUI, goog.events.EventTarget);

hps.prime.BoardUI = function(filledCell, node){
    //  goog.base(this);
    this._isGameOver = false;
    this._board = new hps.prime.BoardFactory.create(filledCell);
    var self = this;
    var items = [];
    for (var y = 0; y < LENGTH_SIZE; y++) {
	for (var x = 0; x < WIDTH_SIZE; x++) {
	    var cell = this._board.get(x,y);
	    var cellDisp = (cell == 0) ? '' : cell;
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


hps.prime.BoardUI.prototype._selectCell = function(palette){
    var idx = palette.getSelectedIndex();
    var x = hps.prime.BoardUI._getX(idx);
    var y = hps.prime.BoardUI._getY(idx);
    var self = this;
    if(!this._isGameOver && this._board.get(x,y) == 0){
	var handler = function(response){
	    if(response != null){
		n = parseInt(response);
		self._board.set(x,y,n);
		self._refreshDisplay();
		goog.dom.setTextContent(palette.getSelectedIndex(), n );
	    }
	};
	var prompt = new goog.ui.Prompt('','Set the value',handler);
	prompt.setValidationFunction(
	    function(input){
		var num = parseInt(input);
		return (num >=0 && num <=9);
	    }
	);
	prompt.setVisible(true);
    }
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




hps.prime.BoardUI.renderEmptyBoard = function(node){
    var items = [];
    for (var y = 0; y < LENGTH_SIZE; y++) {
	for (var x = 0; x < WIDTH_SIZE; x++) {
	    items.push(goog.dom.createTextNode('1'));
	}
    }
    var boardPalette = new goog.ui.Palette(items);
    boardPalette.setSize(3);
    
    boardPalette.render(node);
    goog.dom.classes.add(boardPalette.getElement(),'simple-palette');
    

};




function sayHi() {
  var newHeader = goog.dom.createDom('h1', {'style': 'background-color:#EEE'},
    'Hello world!');
    goog.dom.appendChild(document.body, newHeader);

    var node = goog.dom.createDom('div',{'id':'prime-board'});
    goog.dom.appendChild(document.body, node);
    
    var b = new hps.prime.BoardUI('',node);
}

