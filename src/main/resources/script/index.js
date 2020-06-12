import {dropdownMenu} from './dropdown.js';
import {bar} from './bar.js';
import {readable} from './util.js';

let results = [];
let barDataOptions;
let barData;
let xLabel = "state";
let barOptions = [];

const onBarDataClicked = column => {
    barData = column;
    render();
};

const render = () => {
    const width = +d3.select('svg').attr('width');
    const margin = {top: 50, right: 20, left: 80, bottom: 100};
    const height = +d3.select('svg').attr('height');
    const innerWidth = width - margin.left - margin.right;

    d3.select('#x-menu')
        .call(dropdownMenu, {
        options: barOptions,
        onOptionClicked: onBarDataClicked,
        selectedOption: barData
    });

    d3.select('svg').call(bar, {
            xValue: d => d[xLabel],
            xAxisLabel: xLabel,
            //NOTE: Because non-numeric drop down options are filtered out in load(), this should always
            //resolve the value of the selection into a number for the Y axis.
            yValue: d => d[barData],
            yAxisLabel: barData,
            margin: margin,
            width: width,
            height: height,
            plotData: results
        });

    d3.select('#title')
        .style('padding-left', innerWidth/6 + 'px')
        .style('font-size', '40px')
        .text(readable(xLabel) + "'s " + readable(barData));
}

const load = () => {
    const jsonDataTypesPromise = d3.json('http://localhost:8080/result/datatype');
    const jsonDataPromise = d3.json('http://localhost:8080/result');
    Promise.all([jsonDataTypesPromise, jsonDataPromise])
    .then(([types, serverResults]) => {
        barDataOptions = types;
        results = serverResults;
        //This next step is to only allow numeric fields, ints or floats, into the graph data options
        //as bar charts can't graph string or array values
        barDataOptions.forEach(d => {
            if(d.type === 'int' || d.type === 'float')
                barOptions.push(d.name);
            }
        );
        //Set the initial graph to be the first available bar option
        barData = barOptions[0];
        render();
    });
};

window.onload = load();