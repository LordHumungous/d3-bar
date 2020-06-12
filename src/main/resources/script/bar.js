import {readable, significantDigits} from './util.js';

/*
This method is specifically designed to generate the bar chart agnostic of the data provided.
xValue is a function that will get the specific X value array out of the provided plotData.
xAxisLabel is the name of the specific value that is to be used as the tick label values.
yValue is a function that will get the data field desired to be charted in the bar graph out of plotData
yAxisLabel is the name of the specific field to get out of plotData to graph
margin is an object that defines margin top, left, right, and bottom
width is the overall SVG window width
height is the overall SVG window height
plotData is the array of objects representing the data available to be bar chart plotted
*/

export const bar = (selection, props) => {
    const {
        xValue,
        xAxisLabel,
        yValue,
        yAxisLabel,
        margin,
        width,
        height,
        plotData
    } = props;

    const yAxisTickFormat = number => {
    //Only deal with specific number formats for the ticks if the values are above 1 million
        if(number >= 1000000)
            return d3.format('.' + significantDigits(number) + 's')(number).replace('G', ' Bill').replace('M', 'Mill').replace('T', 'Trill');
        else
            return number;
    }

    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;

    const xScale = d3.scaleBand().domain(d3.range(plotData.length)).range([0, innerWidth]).padding(0.1);
    //This is where the specific tick label values are assigned, representing the column names.
    const xAxis = d3.axisBottom(xScale).tickFormat(i => plotData[i][xAxisLabel]);

    const yScale = d3.scaleLinear().domain([0, d3.max(plotData, yValue)]).range([innerHeight, 0]).nice();
    const yAxis = d3.axisLeft(yScale).tickSize(-innerWidth).tickPadding(10).tickFormat(yAxisTickFormat);

    const g = selection.selectAll('.container').data([null]);
    const gEnter = g.enter()
        .append('g')
        .attr('class', 'container');
    gEnter.merge(g)
            .attr('transform', `translate(${margin.left}, ${margin.top})`);

    const yAxisGroup = g.select('.y-axis');
    const yAxisGroupEnter = gEnter.append('g').attr('class', 'y-axis');

    yAxisGroup
        .merge(yAxisGroupEnter)
            .call(yAxis)
            .selectAll('.domain').remove();

    const yAxisLabelText = yAxisGroupEnter
        .append('text')
            .attr('y', -60)
            .attr('text-anchor', 'middle')
            .attr('transform', `rotate(-90)`)
            .attr('fill', 'black')
            .attr('class', 'y-axis')
        .merge(yAxisGroup.select('.y-axis'))
            .attr('x', -innerHeight/2)
            .text(readable(yAxisLabel));

    const xAxisGroup = g.select('.x-axis');
    const xAxisGroupEnter = gEnter.append('g').attr('class', 'x-axis');
    xAxisGroup.merge(xAxisGroupEnter)
        .attr("transform", `translate(0, ${innerHeight})`)
        .call(xAxis)
        .selectAll("g.tick > text")
                .style("text-anchor", "end")
                //The next four lines handle aligning the tick labels correctly with the tick marks
                .attr("dy", -10)
                .attr("y", 15)
                .attr("dx", -10)
                .attr("transform", "rotate(-65)")
        .selectAll('.domain').remove();

    const rects = g.merge(gEnter)
        .selectAll('rect').data(plotData);
    rects.join('rect')
            .attr("x", (d, i) => xScale(i))
            .attr("y", d => yScale(yValue(d)))
            .attr("height", d => yScale(0) - yScale(yValue(d)))
            .attr("width", xScale.bandwidth());
};