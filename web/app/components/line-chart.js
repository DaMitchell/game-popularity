import Ember from 'ember';
import WindowResizeMixin from './mixin/window-resize';
import GoogleLineChart from 'ember-google-charts/components/line-chart';

const { $, run } = Ember;

export default GoogleLineChart.extend(WindowResizeMixin, {
    onWindowResize: function () {
        this._rerenderChart();
    }
});
