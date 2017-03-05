<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use DB;

class postaggingController extends Controller
{
    public function index(){
    	return view('contents.postagging')->with('postags',DB::table('tb_postagger')->orderBy('id','ASC')->paginate(10));
    }
}
