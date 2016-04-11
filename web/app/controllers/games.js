import Ember from 'ember';

export default Ember.Controller.extend({

    search: "",

    _observeSearch: function() {
        this.get('model').set('name', this.get('search'));
        this.get('model').load();
    }.observes('search'),

    actions: {
        selectGame(game) {
            console.log('game controller', game);
        }
    }
});
