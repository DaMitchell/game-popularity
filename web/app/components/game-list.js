import Ember from 'ember';

export default Ember.Component.extend({
    classNames: ['nano'],

    willRender() {
        console.log('willRender');
        if(Ember.$(this.element)) {
            Ember.$(this.element).nanoScroller({destroy: true}).each(() => delete this.nanoscroller);
        }
    },

    didRender() {
        this._super(...arguments);
        if(!this.get('games.isLoading')) {
            Ember.$(this.element).nanoScroller();
        }
    },

    loading: function() {
        return this.get('games.isLoading');
    }.property('games.isLoading'),

    actions: {
        selectGame(game) {
            console.log('component', game);
            this.sendAction('clickAction', game);
        }
    }
});
