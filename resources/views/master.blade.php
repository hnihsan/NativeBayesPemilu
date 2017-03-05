<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <!-- <link rel="icon" type="image/png" href="assets/img/favicon.ico"> -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <title>Naive Bayes</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />
    <!-- Bootstrap core CSS     -->
    <link href="{{ URL('assets/css/bootstrap.min.css') }} " rel="stylesheet" />
    <!-- Animation library for notifications   -->
    <link href="{{ URL('assets/css/animate.min.css')}}" rel="stylesheet"/>
    <!--  Light Bootstrap Table core CSS    -->
    <link href="{{ URL('assets/css/light-bootstrap-dashboard.css') }}" rel="stylesheet"/>
    <!--  CSS for Demo Purpose, don't include it in your project     -->
    <link href="{{ URL('assets/css/demo.css') }} " rel="stylesheet" />
    <!--     Fonts and icons     -->
    <!-- <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet"> -->
    <!-- <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'> -->
    <link href="{{ URL('assets/font-awesome/css/font-awesome.min.css') }} " rel="stylesheet" />
    <link href="{{ URL('assets/css/pe-icon-7-stroke.css') }} " rel="stylesheet" />
    <link href="{{ URL('assets/plugins/bootstrap-datepicker/css/datepicker3.css') }} " rel="stylesheet" />
    <link href="{{ URL('assets/plugins/clockpicker/clockpicker.css') }} " rel="stylesheet" />
    <link href="{{ URL('assets/plugins/chartist/chartist.min.css') }} " rel="stylesheet" />
    <link rel="stylesheet" href="{{ URL('assets/plugins/morris/morris.css') }}">
    <style type="text/css">

            #exTab1 .tab-content {
              color : white;
              /*background-color: #428bca;*/
              padding : 5px 15px;
            }

            #exTab2 h3 {
              color : white;
              background-color: #428bca;
              padding : 5px 15px;
            }

            /* remove border radius for the tab */

            #exTab1 .nav-pills > li > a {
              border-radius: 0;
            }

            /* change border radius for the tab , apply corners on top*/

            #exTab3 .nav-pills > li > a {
              border-radius: 4px 4px 0 0 ;
            }

            #exTab3 .tab-content {
              color : white;
              /*background-color: #428bca;*/
              padding : 5px 15px;
            }
    </style>
    
</head>
<body>
<div class="wrapper">
    @include('layouts.sidebar')
    <div class="main-panel">
        @include('layouts.navbar')
        <div class="content">
            <div class="container-fluid">
                @yield('content')
            </div>
        </div>
        @include('layouts.footer')
    </div>
</div>
</body>
    <!--   Core JS Files   -->
    <script src="{{ URL('assets/js/jquery-2.1.1.js') }} " type="text/javascript"></script>
    <script src="{{ URL('assets/js/jquery-ui.min.js') }}"></script>
    <script src="{{ URL('assets/js/bootstrap.min.js') }} " type="text/javascript"></script>
    <!--  Checkbox, Radio & Switch Plugins -->
    <script src="{{ URL('assets/js/bootstrap-checkbox-radio-switch.js')}}"></script>
    <!--  Charts Plugin -->
    <script src="{{ URL('assets/js/chartist.min.js') }}"></script>
    <!--  Notifications Plugin    -->
    <script src="{{ URL('assets/js/bootstrap-notify.js') }}"></script>
    <!--  Google Maps Plugin    -->
    <script src="{{ URL('assets/js/light-bootstrap-dashboard.js')}}"></script>
    <script src="{{ URL('assets/plugins/chartist/chartist.min.js') }}"></script>
    <script src="{{ URL('assets/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js') }}"></script>
    <script src="{{ URL('assets/plugins/clockpicker/clockpicker.js') }}"></script>
    <script src="{{ URL('assets/js/demo.js') }}"></script>
    <script src="{{ URL('assets/plugins/morris/morris.min.js') }}"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>

    <script type="text/javascript">
       $('#input-date .input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true
            });
   </script>

    @yield('javascript_chart')
    <script type="text/javascript">
        $(document).ready(function(){
            // demo.initChartist();

            // $.notify({
            //     icon: 'pe-7s-gift',
            //     message: "Naive Bayes"
            // },{
            //     type: 'info',
            //     timer: 4000
            // });
            $('.clockpicker').clockpicker();
        });
    </script>
</html>
