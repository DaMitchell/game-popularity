import Ember from 'ember';

export default Ember.Controller.extend({

    selected: Ember.computed({
        get() {
            return this.get('_selected') ? this.get('_selected') : this.get('model.firstObject');
        },
        set(key, value) {
            return this.set('_selected', value);
        }
    }),

    search: "",

    _observeSearch: function() {
        this.get('model').set('name', this.get('search'));
        this.get('model').load();
    }.observes('search'),

    actions: {
        selectGame(game) {
            this.set('selected', game);
        }
    }
});
