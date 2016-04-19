import Ember from 'ember';
import GameModel from '../models/game';

export default Ember.Controller.extend({

    games: Ember.inject.controller('games'),

    game: function() {
        var selected = this.get('games.selected');
        return selected ? GameModel.find({
            id: selected.get('id'),
            type: 'day'
        }).load() : null;
    }.property('games.selected')
});
