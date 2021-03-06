import Ember from 'ember';

const { $, assert, run } = Ember;

export default Ember.Mixin.create({
    didInsertElement: function () {
        this._super(...arguments);
        $(window).on('resize', {scope:this}, this.onWindowResizeThrottle);
    },

    onWindowResizeThrottle: function (event) {
        run.throttle(event.data.scope, 'onWindowResize', 100);
    },

    onWindowResize: function () {
        assert('You have used the window resize mixin and no created a onWindowResize method');
    },

    willDestroyElement: function () {
        this._super(...arguments);
        $(window).off('resize', this.onWindowResizeThrottle);
    }
});
