goog.provide('hps.prime.Player');

hps.prime.Player = function(index, name, score){
    this._index = index;
    this._name = name;
    this._score = score;
};

hps.prime.Player.prototype.getName = function(){
    return this._name;  
};

hps.prime.Player.prototype.getScore = function(){
    return this._score;
};

hps.prime.Player.prototype.setScore = function(score){
    this._score = score;
};


hps.prime.Player.prototype.addScore = function(score){
    this._score += score;
};


hps.prime.Player.prototype.getIndex = function(){
    return this._index;
};