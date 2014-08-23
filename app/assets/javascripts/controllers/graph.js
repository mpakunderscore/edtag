var width = 910,
    height = 700;

var color = d3.scale.category20();

var force = d3.layout.force()
    .charge(-120)
    .linkDistance(30)
    .size([width, height]);



edTagApp.controller('graphCtrl', function ($scope, $http) {

    $scope.selectedTags = [];

    $scope.filterByTag = function (tag) {
//        filter($scope, tag)
    }
});

edTagApp.directive('ngEnter', function () {

    return function ($scope, $element, $attrs) {
        $element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                $scope.$apply(function () {

                    if ($scope.url == 0) return;

                    graph($scope.url, $scope);
                    $scope.url = ''
                });

                event.preventDefault();
            }
        });
    };
});

function graph(url, $scope) {

    d3.json("/api/graph?url=" + url, function(error, graph) {

        $scope.$apply(function(){ $scope.sortedTags = graph.webData.tags; });

        d3.select("svg").remove();

        var svg = d3.select(".out").append("svg")
            .attr("width", width)
            .attr("height", height);

        force
            .nodes(graph.nodes)
            .links(graph.links)
            .start();

        var link = svg.selectAll(".link")
            .data(graph.links)
            .enter().append("line")
            .attr("class", "link")
            .style("stroke-width", function(d) { return Math.sqrt(d.value); });

        var node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("circle")
            .attr("class", "node")
            .attr("r", 5)
            .style("fill", function(d) { return color(d.group); })
            .call(force.drag);

        node.append("title")
            .text(function(d) { return d.name; });

        force.on("tick", function() {
            link.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });

            node.attr("cx", function(d) { return d.x; })
                .attr("cy", function(d) { return d.y; });
        });
    });
}
