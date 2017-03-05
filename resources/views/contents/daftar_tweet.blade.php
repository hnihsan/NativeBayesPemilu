@extends('master')
@section('content')
<div class="col-md-12">
    {!! Session::get('message') !!}
    <div class="card">
        <div class="header">
            <h4 class="title">Daftar tweet</h4>
            <!-- <p class="category">Here is a subtitle for this table</p> -->
        </div>
        <div class="content table-responsive table-full-width">
            <table class="table table-hover table-striped">
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Tweet</th>
                        <th>tanggal</th>
                        <th>Tambah ke tweet training</th>
                    </tr>
                </thead> 
                <tbody>
                @foreach($tweets as $tweet)
                    <tr>
                    	<td>{{ $tweet->username }}</td>
                        <td>{{ $tweet->tweet }}</td>
                        <td>{{ $tweet->date_tweet }}</td>
                        <td class="text-center">
                        @if(!in_array($tweet->tweet,$tweets_training))
                        <div class="dropdown"> 
                            <button type="button" class="btn btn-info btn-fill text-center dropdown-toggle" id="dropdownMenu1" data-toggle="dropdown"> Tambah <span class="caret"></span> 
                            </button> 
                            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"> 
                                <li role="presentation"> 
                                    <a role="menuitem" tabindex="-1" href="{{URL('/training/add/'.$tweet->id.'/positif')}}">Positif</a> 
                                </li> 
                                <li role="presentation"> 
                                    <a role="menuitem" tabindex="-1" href="{{URL('/training/add/'.$tweet->id.'/negatif')}}">Negatif</a> 
                                </li> 
                                <li role="presentation"> 
                                    <a role="menuitem" tabindex="-1" href="{{URL('/training/add/'.$tweet->id.'/netral')}}">Netral</a> 
                                </li>  
                            </ul> 
                        </div>
                        @else
                        <span class="badge">Sudah ditambahkan</span>
                        @endif
                        </td>
                    </tr>
                @endforeach
                </tbody>
            </table>
            <div class="text-center">
                {!! $tweets->render() !!}    
            </div>
        </div>
    </div>
</div>
@stop