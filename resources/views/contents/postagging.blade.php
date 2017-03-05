@extends('master')
@section('content')
<div class="col-md-12">
    {!! Session::get('message') !!}
    <div class="card">
        <div class="header">
            <h4 class="title">Daftar Postagging</h4>
            <!-- <p class="category">Here is a subtitle for this table</p> -->
        </div>
        <div class="content table-responsive table-full-width">
            <table class="table table-hover table-striped">
                <thead>
                    <tr>
                        <th>Postagging</th>
                        <th>Keterangan</th>
                    </tr>
                </thead>
                <tbody>
                @foreach($results as $postag)
                    <tr>
                    	<td>{{ $postag }}</td>
                        <td class="text-center">
                          Opini/Fakta
                        </td>
                    </tr>
                @endforeach
                </tbody>
            </table>
            <div class="text-center">

            </div>
        </div>
    </div>
</div>
@stop
