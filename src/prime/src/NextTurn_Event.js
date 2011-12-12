goog.provide('hps.prime.NextTurnEvent');

goog.require('hps.prime.EventType');
goog.require('goog.events.Event');


hps.prime.NextTurnEvent = function(player,addScore,target){
    goog.base(this, hps.prime.EventType.NEXT_TURN, target);  
    
    this._player = player;
    this._addScore = addScore;
};
goog.inherits(hps.prime.NextTurnEvent, goog.events.Event);


hps.prime.NextTurnEvent.prototype.getPlayer = function(){
    return this._player;
};

hps.prime.NextTurnEvent.prototype.getAddScore = function(){
    return this._addScore;
};