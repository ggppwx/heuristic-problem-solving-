goog.provide('hps.prime.PlayerList');

goog.require('goog.structs');

hps.prime.PlayerList = function(players){
    this._players = players;
    this._currentIdx = 0;
};


hps.prime.PlayerList.prototype.next = function(){
    var size = this._players.length;
    if( size !== 2){
	throw "not 2 players";
    }
    var nextPlayer = this._players[this._currentIdx++];
    if (this._currentIdx >= size){
	this._currentIdx = this._currentIdx % size;
    }
    return nextPlayer;
};

hps.prime.PlayerList.prototype.getCurrentPlayer = function(){
    var player = null;
    if(this._players.length > 0){
	player = this._players[this._currentIdx];
    }
    return player;
};

hps.prime.PlayerList.prototype.getPlayerCount = function(){
    return this._players.length;
};


hps.prime.PlayerList.prototype.getNextPlayer = function(){
    var size = this._players.length;
    var nextPlayerIdx = this._currentIdx + 1;
    if(nextPlayerIdx >= size){
	return this._players[nextPlayerIdx % size];
    }else{
	return this._players[nextPlayerIdx];
    }
};