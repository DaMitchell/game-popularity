import Ember from 'ember';
import WindowResizeMixin from './mixin/window-resize';

const { $, assert, computed } = Ember;

export default Ember.Component.extend(WindowResizeMixin, {
    viewers: false,
    channels: false,

    classNames: ['game-chart'],

    height: 400,

    options: computed('height', 'viewers', 'channels', function() {
        var options = {
            height: this.get('height'),
            series: {},
            axes: {y: {}},
            legend: {position: 'left', textStyle: {fontSize: 12}},
            pointSize: 5
        };

        if(this.get('viewers')) {
            options.series[0] = {axis: 'Viewers'};
            options.axes.y['Viewers'] = {label: 'Viewers'};
        }

        if(this.get('channels')) {
            options.series[this.get('viewers') ? 1 : 0] = {axis: 'Channels'};
            options.axes.y['Channels'] = {label: 'Channels'};
        }

        if(this.get('height') < 250 || Object.keys(options.series).length === 1) {
            options.legend.position = 'none';
        }

        return options;
    }),

    stats: computed('game.stats', 'viewers', 'channels', function() {
        var columns = [];
        columns.push('Date');

        if(this.get('viewers')) {
            columns.push('Viewers');
        }

        if(this.get('channels')) {
            columns.push('Channels');
        }

        var data = [columns];

        var stats = this.get('game.stats');

        if(!stats) {
            return data;
        }

        data.push.apply(data, Object.keys(stats).map((date) => {
            var data = [new Date(date)];

            ['viewers', 'channels'].forEach((prop) => {
                if(this.get(prop)) {
                    data.push(stats[date][prop]);
                }
            });

            return data;
        }));

        return data;
    }),

    hasStats: computed('stats', function() {
        return this.get('stats.length') > 1;
    }),

    init() {
        this._super(...arguments);
        this._reset();
        //this.addObserver('game', this, this._reset);
        this.addObserver('viewers', this, this._onViewersChanged);
        this.addObserver('channels', this, this._onChannelsChanged);
    },

    didInsertElement() {
        this._super(...arguments);
        this.set('height', parseInt(this.$('.chart-body').css('height'), 10));
    },

    onWindowResize() {
        this.set('height', parseInt(this.$('.chart-body').css('height'), 10));
    },

    _reset() {
        this.set('viewers', true);
        this.set('channels', false);
    },

    _onViewersChanged() {
        if(!this.get('viewers') && !this.get('channels')) {
            this.set('viewers', this);
        }
    },

    _onChannelsChanged() {
        if(!this.get('viewers') && !this.get('channels')) {
            this.set('channels', this);
        }
    }
});
