import Ember from 'ember';
import Games from '../models/games';

export default Ember.Route.extend({
    model() {
        //return [{name:"test route"}];
        return Games.find({type:"search"}).load(true);
    },

    renderTemplate(controller, model) {
        this.render('index');
        this.render('games', {
            outlet: 'right',
            model: model
        });
    }
});
