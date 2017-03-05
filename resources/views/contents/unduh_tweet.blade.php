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
                <form method="POST" action="{{ action('TweetController@unduh') }}">
                    {!! csrf_field() !!}
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group text-center">
                                <!-- <label>First Name</label> -->
                                <!-- <input type="text" class="form-control" placeholder="Company" value=""> -->
                                 <button type="submit" class="btn btn-info btn-fill text-center">Mulai unduh tweet</button>
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