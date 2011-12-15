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
    this._move = 0;
    this._isPlaying = false;    


    this._statusBarDom = goog.dom.createDom('div',{id:'status-bar'});
    goog.dom.setTextContent(this._statusBarDom, GAME_MSG);    
    goog.dom.appendChild(node, this._statusBarDom);
    

    var winnerBarDom = goog.dom.createDom('div',{id:'winner-bar-status'});
    this._winnerDom = goog.dom.createDom('div',{id:'winner-bar'});
    goog.dom.setTextContent(this._winnerDom,'This is winner board');

    goog.dom.appendChild(winnerBarDom,this._winnerDom);
    goog.dom.appendChild(node,winnerBarDom);


    this._player1NameDom = goog.dom.createDom('div',{id:'score-bar-player-name'});
    this._player1ScoreDom = goog.dom.createDom('div',{id:'score-bar-player-score'});
    this._player1Dom = goog.dom.createDom('div',{id:'score-bar'},
					  this._player1NameDom,this._player1ScoreDom);
    goog.dom.setTextContent(this._player1NameDom,'Player1: ');


    this._player2NameDom = goog.dom.createDom('div',{id:'score-bar-player-name'});
    this._player2ScoreDom = goog.dom.createDom('div',{id:'score-bar-player-score'});
    this._player2Dom = goog.dom.createDom('div',{id:'score-bar'},
					 this._player2NameDom, this._player2ScoreDom);
    goog.dom.setTextContent(this._player2NameDom,'Player2: ');

    

    
    this._scoreBarDom = goog.dom.createDom('div','score-bars',
					   this._player1Dom,this._player2Dom
					   );
    goog.dom.appendChild(node, this._scoreBarDom);
    
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
    

    var btnBarDom = goog.dom.createDom('div',{id: 'btn-bar'});
    // add button
    var startGameDom = goog.dom.createDom('div',{id: 'start-game-btn'});
    this._startGameBtn = new goog.ui.Button('start game',
					    goog.ui.FlatButtonRenderer.getInstance());
    this._startGameBtn.render(startGameDom);
    goog.dom.appendChild(btnBarDom, startGameDom);

    // add button
    /*
    var saveGameDom = goog.dom.createDom('div',{id: 'save-game-btn'});
    this._saveScoreBtn = new goog.ui.Button('save game',
					    goog.ui.FlatButtonRenderer.getInstance());
    this._saveScoreBtn.render(saveGameDom);
    this._saveScoreBtn.setEnabled(false);
    goog.dom.appendChild(btnBarDom, saveGameDom);
     */

    goog.dom.appendChild(node,btnBarDom);
    
    // listening to event 
    var handleStartBtn = function(e){
	//  TODO
	// here this doesn't mean this file !!!!!!
	// a good way is to save this point at the begining of the constructor
	var selfRef = self;
	if(selfRef._isPlaying){
	    // click stop game
	    goog.dom.setTextContent(selfRef._statusBarDom,'Game Stopped');
	    selfRef._playerListUI.enable();
	    selfRef._disposeBoard();
	    goog.dom.setTextContent(selfRef._startGameBtn.getElement(), 'start game');
	    // selfRef._saveScoreBtn.setEnabled(false);
	}else{
	    // is not playing, click  the button, start the game
	    if(selfRef._playerListUI.getPlayerCount() != 2){
		goog.dom.setTextContent(selfRef._statusBarDom,
					'ERROR: the player number is not 2, could not start');
		return;
	    }

	    selfRef._createBoard();
	    // disable ui
	    selfRef._playerListUI.disable();
	    goog.dom.setTextContent(selfRef._startGameBtn.getElement(), 'stop game');
	    // selfRef._saveScoreBtn.setEnabled(false);
	   
	}
	selfRef._isPlaying = !selfRef._isPlaying;

    };
    
    goog.events.listen(this._startGameBtn, goog.ui.Component.EventType.ACTION,
		      handleStartBtn);
     
    /* 
    var handleSaveBtn = function(e){
	// TODO
	var selfRef = self;
	

	selfRef._saveScoreBtn.setEnabled(false);
    };
    
    goog.events.listen(this._saveScoreBtn, goog.ui.Component.EventType.ACTION,
		      handleSaveBtn);
     */
};



hps.prime.MainUI.prototype._createBoard = function(){
    // player store the information of each player.
    // recreate player list which stores player information
    this._move = 0;
    this._activePlayerList = this._playerListUI.getPlayerList();
    if(this._activePlayerList.length !== 2){
	goog.dom.setTextContent(this._statusBarDom,
				'ERROR: the player number is not 2, could not start');
    }else{
	goog.dom.removeChildren(this._boardDom);
	goog.dom.setTextContent(this._statusBarDom,
				'Game start: '+
				this._activePlayerList[0].getName()+' moves');
	
	var p1 = this._activePlayerList[0];
	var p2 = this._activePlayerList[1];
	
	goog.dom.setTextContent(this._player1NameDom,'Player1: '+p1.getName());
	goog.dom.setTextContent(this._player2NameDom,'Player2: '+p2.getName());
	goog.dom.setTextContent(this._player1ScoreDom, '0');
	goog.dom.setTextContent(this._player2ScoreDom, '0');
	p = new hps.prime.PlayerList([p1,p2]);
	this._gamestate = new hps.prime.BoardUI('',p,this._boardDom);

	// listening to gameover event etc 
	goog.events.listen(this._gamestate, hps.prime.EventType.GAME_OVER,
			   this._handleGameOver,false, this);
	
	goog.events.listen(this._gamestate, hps.prime.EventType.NEXT_TURN,
		      this._handleNextTurn, false, this);


	goog.dom.setTextContent(this._winnerDom,'This is winner board');
	
    }
};


/*
 * game over handler
 * */
hps.prime.MainUI.prototype._handleGameOver = function(e){
    // var message = goog.string.buildString('Game Over: ');
    // show who wins 
    var player1 = this._activePlayerList[0];
    var player2 = this._activePlayerList[1];

    if(player1.getScore() > player2.getScore()){
	goog.dom.setTextContent(this._winnerDom, 
				'Game over, '+player1.getName()+' wins!');
    }else if(player1.getScore() < player2.getScore()){
	goog.dom.setTextContent(this._winnerDom, 
				'Game over, '+player2.getName()+' wins!');
    }else{
	goog.dom.setTextContent(this._winnerDom, 
				'Game over, tie!');
    }

    goog.dom.setTextContent(this._startGameBtn.getElement(),'start game');
    this._playerListUI.enable();
    this._isPlaying = false;
    // this._saveScoreBtn.setEnabled(true);
    
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

    var idx = player.getIndex();
    
    this._activePlayerList[idx].addScore(addScore);
    
    var score1 = this._activePlayerList[0].getScore();
    var score2 = this._activePlayerList[1].getScore();
    
    var text1 = goog.dom.getTextContent(this._player1Dom);
    var text2 = goog.dom.getTextContent(this._player2Dom);

    goog.dom.setTextContent(this._player1ScoreDom, score1.toString());
    goog.dom.setTextContent(this._player2ScoreDom, score2.toString());
    // show who's turn.  (next player)
    var size =  this._activePlayerList.length;
    var nextIdx = -1;
    if(this._move == 7){
	nextIdx = idx;
    }else{
	nextIdx = idx + 1 >= size ? (idx+1) % size : idx+1;
    }
    goog.dom.setTextContent(this._statusBarDom, 
			    player.getName()+'+'+addScore.toString()+' Now '+
    			    this._activePlayerList[nextIdx].getName()+' moves ');
    
    this._move ++;
};


/*
 * dispose the board
 * */
hps.prime.MainUI.prototype._disposeBoard = function(){
    goog.dom.removeChildren(this._boardDom);
    hps.prime.BoardUI.createEmptyBoard(this._boardDom);
    goog.dom.setTextContent(this._player1NameDom,'Player1: ');
    goog.dom.setTextContent(this._player2NameDom,'Player1: ');
    goog.dom.setTextContent(this._player1ScoreDom, '');
    goog.dom.setTextContent(this._player2ScoreDom, '');
    this._gamestate = null;
};



hps.prime.MainUI.getWinnerBoardContent = function(){
    var content = goog.dom.getTextContent(goog.dom.getElement('winner-bar'));

    goog.dom.setTextContent(goog.dom.getElement('winner-bar'),'This is winner board');
    return content;
};