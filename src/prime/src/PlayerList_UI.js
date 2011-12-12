goog.provide('hps.prime.PlayerListUI');

goog.require('goog.string');

goog.require('goog.dom');
goog.require('goog.ui.LabelInput');

goog.require('goog.events');
goog.require('goog.events.EventType');

goog.require('goog.ui.Button');
goog.require('goog.ui.FlatButtonRenderer');

goog.require('hps.prime.Player');
goog.require('hps.prime.PlayerList');

/*
 * 
 * */
hps.prime.PlayerListUI = function(node){
    self = this;

    addInputDom = goog.dom.createDom('div',{id:'add-input'});
    this._addInput = new goog.ui.LabelInput();
    


    addPlayerDom = goog.dom.createDom('div', {id: 'add-player-btn'});
    this._addPlayerBtn = new goog.ui.Button('add player',
				       goog.ui.FlatButtonRenderer.getInstance());
    
    this._playerTableDom = goog.dom.createDom('div',{id:'player-table'});
    
    

    GUIDom = goog.dom.createDom('div', {id:'player-list'},
				    addInputDom, addPlayerDom, this._playerTableDom);
    
    goog.dom.appendChild(node, GUIDom);
    
    this._addInput.render(addInputDom);   
    this._addPlayerBtn.render(addPlayerDom);

    goog.events.listen(this._addPlayerBtn,goog.ui.Component.EventType.ACTION,
		      this._addPlayerHandler,false, this);

};



hps.prime.PlayerListUI.prototype._addPlayerHandler = function(e){
    var inputName = this._addInput.getValue();
    var rmBtn;
    nameDom = goog.dom.createDom('div','player-list-entry-name');
    goog.dom.setTextContent(nameDom, inputName);
    
    btnDom = goog.dom.createDom('div','player-list-entry-btn');
    
    playerDom = goog.dom.createDom('div',{id:'player-list-entry'},nameDom,btnDom);

    


    goog.dom.appendChild(this._playerTableDom, playerDom);
};



hps.prime.PlayerListUI.prototype.getPlayerList = function(){
    // TODO
    
};


hps.prime.PlayerListUI.prototype.disable() = function(){
    this._addPlayerBtn.setEnabled(false);  
};

hps.prime.PlayerListUI.prototype.enable() = function(){
    this._addPlayerBtn.setEnabled(true);
};