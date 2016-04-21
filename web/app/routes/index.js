import Ember from 'ember';
import GamesModel from '../models/games';
import GameModel from '../models/game';


export default Ember.Route.extend({
    model() {
        return GamesModel.find({type:"search"}).load(true);
    },

    setupController(controller, model) {
        GameModel.find({
            id: model.get('firstObject').get('id'),
            type: controller.get('type')
        }).load(true).then((game) => controller.set('game', game));
    },

    renderTemplate(controller, model) {
        this.render('index');
        this.render('games', {
            outlet: 'right',
            model: model
        });
    }
});
