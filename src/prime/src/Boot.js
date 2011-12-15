goog.provide('hps.prime.Boot');


goog.require('goog.dom');
goog.require('hps.prime.MainUI');


hps.prime.Boot.run =function() {
   var b = goog.dom.getElement('board');
 var ui = new hps.prime.MainUI(b);
};

