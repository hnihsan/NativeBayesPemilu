<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Storage;

class postaggingController extends Controller
{
    public function index(){
      $postagging = Storage::get('public/pos_tagger_result.txt');
      $result = explode("\n", $postagging);
      array_shift($result);
    	return view('contents.postagging')->with('results',$result);
    }
}
