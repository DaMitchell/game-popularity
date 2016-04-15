import Ember from 'ember';
import GameModel from '../models/game';

export default Ember.Controller.extend({

    games: Ember.inject.controller('games'),

    game: function() {
        var selected = this.get('games.selected');
        return selected ? GameModel.find({id: selected.get('id')}).load() : null;
    }.property('games.selected'),

    isGameSetAndLoaded: function() {
        return this.get('game') && !this.get('game.isLoading');
    }.property('game', 'game.isLoading'),

    gameChartData: function() {
        var stats = this.get('game.stats');

        if(!stats) {
            return [];
        }

        var data = [['Date', 'Viewers', 'Channels']];
        data.push.apply(data, Object.keys(stats).map((date) => [
            new Date(date),
            stats[date].viewers,
            stats[date].channels,
        ]));

        return data;
        //return this.get('game.stats');
    }.property('game.stats'),

    chartOptions: {
        height: 400,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {
                axis: 'Viewers',
                pointsVisible: true
            },
            1: {
                axis: 'Channels'
            }
        },
        axes: {
            // Adds labels to each axis; they don't have to match the axis names.
            y: {
                Viewers: {label: 'Viewers'},
                Channels: {label: 'Channels'}
            }
        },
        legend: {position: 'left', textStyle: {fontSize: 12}},
        pointSize: 5
    },

    actions: {
        emptyGames() {
            this.get('model').empty();
        }
    }
});
