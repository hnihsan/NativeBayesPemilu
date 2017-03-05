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
                <form method="POST" action="{{ action('TweetController@storeTraining') }}">
                    {!! csrf_field() !!}
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group text-center {{ $errors->has('tweet') ? ' has-error' : '' }}">
                                <label>Tweet training</label>
                                <textarea class="form-control" name="tweet">{{ old('tweet') }}</textarea>
                                <span class="text-danger">{{ $errors->first('tweet')}}</span>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="form-group text-center {{ $errors->has('tweet') ? ' has-error' : '' }}">
                                <label>Tipe tweet training</label><br/>
                                <label class="checkbox-inline"> 
                                    <input type="radio" name="tipetraining" value="p" {{ (old('tipetraining')=='n') ? 'checked' : ""}}> Positif 
                                </label> 
                                <label class="checkbox-inline"> 
                                    <input type="radio" name="tipetraining" value="n" {{ (old('tipetraining')=='p') ? 'checked' : ""}}> Netral 
                                </label>
                                <label class="checkbox-inline"> 
                                    <input type="radio" name="tipetraining" value="ng" {{ (old('tipetraining')=='ng') ? 'checked' : ""}}> Negatif 
                                </label><br/>
                                <span class="text-danger">{{ $errors->first('tipetraining')}}</span>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="form-group text-center">
                                 <button type="submit" class="btn btn-info btn-fill text-center">Simpan</button>
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