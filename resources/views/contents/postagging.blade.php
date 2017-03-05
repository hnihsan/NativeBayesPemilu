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
                        <th>Tambah Rule Opini</th>
                    </tr>
                </thead> 
                <tbody>
                @foreach($postags as $postag)
                    <tr>
                    	<td>{{ $postag->hsl_postagger }}</td>
                        <td class="text-center">
                        <div class="dropdown"> 
                            <button type="button" class="btn btn-info btn-fill text-center dropdown-toggle" id="dropdownMenu1" data-toggle="dropdown"> Tambah <span class="caret"></span> 
                            </button> 
                            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"> 
                                <li role="presentation"> 
                                    <a role="menuitem" tabindex="-1" href="Positif"</a> 
                                </li> 
                                <li role="presentation"> 
                                    <a role="menuitem" tabindex="-1" href="Negatif"</a> 
                                </li> 
                                <li role="presentation"> 
                                    <a role="menuitem" tabindex="-1" href="Netral"</a> 
                                </li>  
                            </ul> 
                        </div>
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