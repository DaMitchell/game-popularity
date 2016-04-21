import Ember from 'ember';
import GameModel from '../models/game';

const { inject, computed } = Ember;

export default Ember.Controller.extend({
    games:  inject.controller('games'),
    type: 'day',

    game: GameModel.create({}, true),

    init(){
        this.get('games').addObserver('selected', this, this.onSelected);
    },

    onSelected() {
        GameModel.find({
            id: this.get('games.selected').get('id'),
            type: this.get('type')
        }).load(true).then((game) => this.set('game', game));
    }
});
