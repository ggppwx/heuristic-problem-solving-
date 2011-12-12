goog.provide('hps.prime.EventType');

goog.require('goog.events');

/*
 * @enum {string}
 * */
hps.prime.EventType = {
    GAME_OVER: goog.events.getUniqueId('Game Over'),
    NEXT_TURN: goog.events.getUniqueId('Next Turn')
};
