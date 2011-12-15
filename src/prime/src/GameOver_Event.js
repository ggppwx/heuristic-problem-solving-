goog.provide('hps.prime.GameOverEvent');

goog.require('hps.prime.EventType');
goog.require('goog.events.Event');

hps.prime.GameOverEvent = function(target){
    goog.base(this,hps.prime.EventType.GAME_OVER, target);
    
};


goog.inherits(hps.prime.GameOverEvent, goog.events.Event);

