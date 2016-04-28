import Ember from 'ember';

export default Ember.Component.extend({
    classNames: ['nano'],

    willRender() {
        if(Ember.$(this.element)) {
            Ember.$(this.element).nanoScroller({destroy: true}).each(() => delete this.nanoscroller);
        }
    },

    didRender() {
        this._super(...arguments);
        if(!this.get('games.isLoading')) {
            Ember.$(this.element).nanoScroller({preventPageScrolling: true});
        }
    },

    loading: function() {
        return this.get('games.isLoading');
    }.property('games.isLoading'),

    actions: {
        selectGame(game) {
            this.sendAction('clickAction', game);
        }
    }
});
