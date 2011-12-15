goog.provide('hps.prime.PlayerListUI');

goog.require('goog.string');

goog.require('goog.dom');
goog.require('goog.ui.LabelInput');

goog.require('goog.events');
goog.require('goog.events.EventType');

goog.require('goog.ui.CustomButton');
goog.require('goog.ui.Button');
goog.require('goog.ui.FlatButtonRenderer');

goog.require('hps.prime.Player');
goog.require('hps.prime.PlayerList');

/*
 * 
 * */
hps.prime.PlayerListUI = function(node){
    self = this;
    
    this._names = [];


    addInputDom = goog.dom.createDom('div',{id:'add-input'});
    this._addInput = new goog.ui.LabelInput();
    


    addPlayerDom = goog.dom.createDom('div', {id: 'add-player-btn'});
    this._addPlayerBtn = new goog.ui.Button('add player',
				       goog.ui.FlatButtonRenderer.getInstance());
    
    this._playerTableDom = goog.dom.createDom('div','player-table');
    
    

    GUIDom = goog.dom.createDom('div', {id:'player-list'},
				    addInputDom, addPlayerDom, this._playerTableDom);
    
    goog.dom.appendChild(node, GUIDom);
    
    this._addInput.render(addInputDom);   
    this._addPlayerBtn.render(addPlayerDom);

    goog.events.listen(this._addPlayerBtn,goog.ui.Component.EventType.ACTION,
		      this._addPlayerHandler,false, this);

};



hps.prime.PlayerListUI.prototype._addPlayerHandler = function(e){
    // check if the name exists
    var selfRef = this;
    
    if(this._names.length >= 2){
	alert('cannot add more player');
	this._addInput.clear();
	return;
    }

    var inputName = this._addInput.getValue();
    if(inputName.length > 8 || inputName.length <=0){
	alert('the name is too long or too short:(');
	this._addInput.clear();
	return;
    }

    if(!this.hasTheUniqName(inputName)){
	alert('the name cannot be the same :( ');
	this._addInput.clear();
	return;
    }
    
    var nameDom = goog.dom.createDom('div','player-list-entry-name');
    goog.dom.setTextContent(nameDom, inputName);
    
    var btnDom = goog.dom.createDom('div','player-list-entry-btn');
    rmBtn = new goog.ui.Button('remove');
    // rmBtn.addClassName('player-list-icon');
    
    //rmBtn.addClassName('goog-custom-button');
    //rmBtn.addClassName('player-list-rm');
    rmBtn.render(btnDom);
   
    var playerDom = goog.dom.createDom('div','player-list-entry',nameDom,btnDom);
   
    goog.dom.appendChild(this._playerTableDom, playerDom);


    goog.events.listen(rmBtn, goog.ui.Component.EventType.ACTION, function(e){
		       goog.dom.removeNode(playerDom);
		       if(!selfRef.deleteName(inputName)){
			   alert("error cannot delete the name");
		       }	   
		       });

    this._names.push(inputName);    
    this._addInput.clear();
    
};



hps.prime.PlayerListUI.prototype.getPlayerList = function(){
    var playerList = [];
    var rowDom =  goog.dom.getFirstElementChild(this._playerTableDom);
    while(rowDom != null){
        var name = goog.dom.getTextContent(goog.dom.getFirstElementChild(rowDom));
	playerList.push(new hps.prime.Player(playerList.length,name,0));	
	rowDom = goog.dom.getNextElementSibling(rowDom);
    }
    return playerList;
};


hps.prime.PlayerListUI.prototype.disable = function(){
    this._addPlayerBtn.setEnabled(false);  
    var rowDom =  goog.dom.getFirstElementChild(this._playerTableDom);
    while(rowDom != null){
	var sth = goog.dom.getLastElementChild(rowDom);
	var btn = goog.dom.getFirstElementChild(sth);
	btn.disabled = true;
	rowDom = goog.dom.getNextElementSibling(rowDom);
    }
};

hps.prime.PlayerListUI.prototype.enable = function(){
    this._addPlayerBtn.setEnabled(true);
    var rowDom =  goog.dom.getFirstElementChild(this._playerTableDom);
    while(rowDom != null){
	var sth = goog.dom.getLastElementChild(rowDom);
	var btn = goog.dom.getFirstElementChild(sth);
	btn.disabled = false;
	rowDom = goog.dom.getNextElementSibling(rowDom);
    }
};



hps.prime.PlayerListUI.prototype.getPlayerCount = function(){
    return goog.dom.getChildren(this._playerTableDom).length;
};





hps.prime.PlayerListUI.prototype.hasTheUniqName = function(name){
    for(var i = 0; i<this._names.length; i++){
	if(name === this._names[i]){
	    return false;
	}
    }
    return true;
};


hps.prime.PlayerListUI.prototype.deleteName = function(name){
    for(var i = 0; i<this._names.length; i++){
	if(name === this._names[i]){
	    this._names.splice(i,1);
	    return true;
	}
    }
    return false;
};

