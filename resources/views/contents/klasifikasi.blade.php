@extends('master')
@section('content')
<div class="row">
	<div class="col-md-6 col-md-offset-3">
        {!! Session::get('message') !!}
        <div class="card">
            <div class="header">
                <h4 class="title"></h4>
            </div>
            <div class="content">
                <form method="POST" action="{{ action('KlasifikasiController@klasifikasi') }}">
                    {!! csrf_field() !!}
                    <div class="row" id="input-date">
                    <div class="col-md-12">
                            <div class="input-group">
                            <label>Tanggal Tweet</label>
                            </div>
                            </div>
                    
                        <div class="col-md-12">
                            <div class="input-group date">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" name="date" class="form-control datepicker" value="{{ date('m/d/Y') }}">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                    <div class="col-md-12">
                            <div class="input-group">
                            <label>Waktu Awal Tweet</label>
                            </div>
                            </div>
                        <div class="col-md-12">
                            <div class="input-group clockpicker" data-autoclose="true">
                                <span class="input-group-addon">
                                    <span class="fa fa-clock-o"></span>
                                </span><input type="text" class="form-control" value="{{ old('start_time_tweet') ? old('start_time_tweet') : '00:00' }}" name="start_time_tweet">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                    <div class="col-md-12">
                            <div class="input-group">
                            <label>Waktu Akhir Tweet</label>
                            </div>
                            </div>
                        <div class="col-md-12">
                            <div class="input-group clockpicker" data-autoclose="true">
                                <span class="input-group-addon">
                                    <span class="fa fa-clock-o"></span>
                                </span><input type="text" class="form-control" value="{{ old('end_time_tweet') ? old('end_time_tweet') : '00:30' }}" name="end_time_tweet">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group text-center">
                                <!-- <label>First Name</label> -->
                                <!-- <input type="text" class="form-control" placeholder="Company" value=""> -->
                                 <button type="submit" class="btn btn-info btn-fill text-center">Mulai Analisis</button>
                            </div>
                        </div>
                    </div>
                   <!--  <button type="submit" class="btn btn-info btn-fill pull-right">Submit</button> -->
                    <div class="clearfix"></div>
                </form>
            </div>
        </div>
    </div>
</div>
@stop