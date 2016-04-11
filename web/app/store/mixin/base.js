import Ember from 'ember';

export default Ember.Mixin.create({
    moreToLoad: true,

    init() {
        this._super();
        this.empty();
    },

    load(returnPromise) {
        if (this.shouldLoad()) {
            this.empty();
            return this.loadMore(returnPromise);
        }
        else {
            return this;
        }
    },

    loadMore(returnPromise) {
        var self = this;

        if (!this.get('moreToLoad')) {
            return this;
        }

        if (this.get('isLoading')) {
            return this;
        }

        this.set('isLoading', true);
        this.set('error', null);

        var request = self.request();

        if (request) {
            var promise = new Ember.RSVP.Promise(function(resolve, reject) {
                request.then(function(response) {
                    self.justGotData();

                    if (self.afterSuccess) {
                        self.afterSuccess(response);
                    }

                    self.set('isLoading', false);

                    resolve(self);
                }, function(error) {
                    self.set('error', error);

                    if (self.afterFail) {
                        self.afterFail(error);
                    }

                    self.set('isLoading', false);

                    reject(error);
                });
            });

            if (returnPromise) {
                return promise;
            }
        }
        else {
            this.set('isLoading', false);
        }

        return this;
    }
});
