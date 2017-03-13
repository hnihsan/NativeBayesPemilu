<?php

namespace App\Http\Controllers;

use  App\Http\Controllers\preprocessing\Stemmer;
use DB;
use Storage;
use  App\Http\Controllers\posTagger\posTagger;

class TestjavaController extends Controller
{
     public function jalanin(){
       $dir=storage_path('app/public/');

       $execute = exec("cd ipostagger/ ; java ipostagger ".storage_path('app/public/')."bahan.txt 1 1 0 1 > ".storage_path('app/public/')."pos_tagger_result2.txt");

       $status="";
       if($execute){
         $status="Berhasil Jalanin File Java";
       }else{
         $status="Gagal .";
       }
       $storage=storage_path('app/public/postagjava/');
       return view('contents.testjava') ->with('status',$status)
                                        ->with('storage',$storage);
     }
}
