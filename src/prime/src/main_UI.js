goog.provide('hps.prime.MainUI');

goog.require('hps.prime.PlayerListUI');

goog.require('hps.prime.PlayerList');
goog.require('hps.prime.Player');

goog.require('hps.prime.BoardUI');
goog.require('hps.prime.EventType');

goog.require('goog.Uri');
goog.require('goog.dom');
goog.require('goog.string');
goog.require('goog.string.StringBuffer');

goog.require('goog.events');
goog.require('goog.events.EventType');
goog.require('goog.ui.Button');
goog.require('goog.ui.FlatButtonRenderer');


var GAME_MSG = 'add two players then presss start to begin';



hps.prime.MainUI = function(node){
    var self = this;
    this._statusBarDom = goog.dom.createDom('div',{id:'status-bar'});
    goog.dom.setTextContent(this._statusBarDom, GAME_MSG);    
    goog.dom.appendChild(node, this._statusBarDom);
    
    
    // create a board UI
    this._gamestate = null;
    this._boardDom = goog.dom.createDom('div',{id:'prime-board'});
    // goog.dom.appendChild(node, this._boardDom);
    hps.prime.BoardUI.createEmptyBoard(this._boardDom);
    // hps.prime.BoardUI.createEmptyBoard(this._boardDom);
    
    // crate a player list UI
    this._sideBarDom = goog.dom.createDom('div',{id:'prime-sidebar'});
    this._playerListUI = new hps.prime.PlayerListUI(this._sideBarDom);

    // put board and sidebar together 
    var boardAndSideDom  = goog.dom.createDom('div',{id:'board-and-sidebar'},
					     this._boardDom, this._sideBarDom);
    goog.dom.appendChild(node, boardAndSideDom);
    
    
    var startGameDom = goog.dom.createDom('div',{id: 'start-game-btn'});
    this._startGameBtn = new goog.ui.Button('start game',
					   goog.ui.FlatButtonRenderer.getInstance());
    this._startGameBtn.render(startGameDom);
    goog.dom.appendChild(node, startGameDom);

    
    var saveGameDom = goog.dom.createDom('div',{id: 'save-game-btn'});
    this._saveScoreBtn = new goog.ui.Button('save game',
					    goog.ui.FlatButtonRenderer.getInstance());
    
    this._saveScoreBtn.render(saveGameDom);
    goog.dom.appendChild(node, saveGameDom);


    
    // listening to event 
    var handleStartBtn = function(e){
	//  TODO
	// here this doesn't mean this file !!!!!!
	// a good way is to save this point at the begining of the constructor
	self._createBoard();
    };
    
    goog.events.listen(this._startGameBtn, goog.ui.Component.EventType.ACTION,
		      handleStartBtn);
     
     
    var handleSaveBtn = function(e){
	// TODO
    };
    goog.events.listen(this._saveScoreBtn, goog.ui.Component.EventType.ACTION,
		      handleSaveBtn);
};



hps.prime.MainUI.prototype._createBoard = function(){
    goog.dom.removeChildren(this._boardDom);
    // player store the information of each player.
    this._activePlayerList = this._playerListUI.getPlayerList();

    p1 = new hps.prime.Player('jack',0);
    p2 = new hps.prime.Player('roy', 0);
    p = new hps.prime.PlayerList([p1,p2]);
    this._gamestate = new hps.prime.BoardUI('',p,this._boardDom);

    // listening to gameover event etc 
    goog.events.listen(this._gamestate, hps.prime.EventType.GAME_OVER,
		      this._handleGameOver,false, this);

    goog.events.listen(this._gamestate, hps.prime.EventType.NEXT_TURN,
		      this._handleNextTurn, false, this);


};


/*
 * game over handler
 * */
hps.prime.MainUI.prototype._handleGameOver = function(e){
    // var message = goog.string.buildString('Game Over: ');
    // show who wins 
    // goog.dom.setTextContent(this._statusBarDom, 'game over');

};


/*
 * next turn handler
 * */
hps.prime.MainUI.prototype._handleNextTurn = function(e){
    var player = e.getPlayer();
    var addScore = e.getAddScore();
    // alert(player.getName());
    // show each player's score 
    goog.dom.setTextContent(this._statusBarDom, player.getName()+'+'+addScore.toString());   

    // show who's turn.  (next player)
    
};


