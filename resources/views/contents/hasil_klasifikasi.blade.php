@extends('master')
@section('content')
{!! Session::get('message') !!}
@foreach($tweets as $analisis => $tweets)
<div class="col-md-12">
    <div class="card">
        <div class="header">
            <h3 class="title">Jumlah Tweet {{ ucfirst($analisis) }}  <span>({{ count($tweets) }})</span></h4>
            <!-- <p class="category">Here is a subtitle for this table</p> -->
        </div>
        <div class="content table-responsive table-full-width">
        <div style="height: 300px; overflow: scroll;">
            <table class="table table-hover table-striped">
                <thead>
                    <tr><th>Tweet</th>
                </tr></thead>
                <tbody>

                	@foreach($tweets as $tweet)
                    <tr>
                        
                    	<td>{{ $tweet }}</td>
                    </tr>
                    @endforeach
                </tbody>
            </table>
            </div>
        </div>
    </div>
</div>
@endforeach
<div class="col-xs-6 text-center col-md-offset-2">
    <h1>Hasil Analisis Tweet</h6>
    <button type="button" class="btn btn-info btn-fill text-center" id="donuts">Donuts</button>
    <button type="button" class="btn btn-info btn-fill text-center" id="bar">Batang</button>

    <div id="ct-chart5" class="ct-perfect-fourth" style="margin-bottom:30px;"></div>
    <span style="background-color:#fff; padding:5px;" id="desc-chart5">
        <p style="color:#4d7904;"> Positif : {{ $count_analisis[0] }}</p>
        <p style="color:#f10614;"> Negatif : {{ $count_analisis[1] }}</p>
        <p style="color:#999;"> Netral : {{ $count_analisis[2] }}</p>
    </span>
    <div id="graph"></div>
</div>
@stop

@section('javascript_chart')
<script type="text/javascript">
    
    $(document).ready(function(){
            $('#graph').hide();
            $('#donuts').click(function(){
                $('#graph').hide();
                $('#ct-chart5').show();
                $('#desc-chart5').show();

            });
            $('#bar').click(function(){
                $('#graph').show();
                $('#ct-chart5').hide();
                $('#desc-chart5').hide();

            });
            var positif = {{ isset($count_analisis) ? $count_analisis[0] : '' }};
            var negatif = {{ isset($count_analisis) ? $count_analisis[1] : '' }};
            var netral = {{ isset($count_analisis) ? $count_analisis[2] : '' }};
            var data = {
                series: [positif, negatif, netral]
            };

            var sum = function(a, b) { return a + b };

            new Chartist.Pie('#ct-chart5', data, {
                labelInterpolationFnc: function(value) {
                    return Math.round(value / data.series.reduce(sum) * 100) + '%';
                }
            });
            // Use Morris.Bar
            Morris.Bar({
              element: 'graph',
              data: [
                {x: 'POSITIF', y: positif},
                {x: 'NETRAL', y: netral},
                {x: 'NEGATIF', y: negatif}
              ],
              xkey: 'x',
              ykeys: ['y'],
              labels: ['Y'],
              barColors: function (row, series, type) {
                if (type === 'bar') {
                  var red = Math.ceil(255 * row.y / this.ymax);
                  return 'rgb(' + red + ',0,0)';
                }
                else {
                  return '#000';
                }
              }
            });
    });

</script>
@stop