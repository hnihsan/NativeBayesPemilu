<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/

Route::get('/', function () {
    return view('contents.home');
});
Route::get('/klasifikasi', function () {
    return view('contents.klasifikasi');
});

Route::get('/tweet','TweetController@index');
Route::get('/tweet/preprocessing','TweetController@preprocessing');
Route::get('/tweet/unduh', function () {
    return view('contents.unduh_tweet');
});
Route::get('/preprocessing', function () {
    return view('contents.preprocessing_form');
});


Route::post('/klasifikasi/store','KlasifikasiController@klasifikasi');
Route::post('/preprocessing','KlasifikasiController@preprocessing');
Route::post('/unduh','TweetController@unduh');

// Route tweet training

Route::get('/training/add', function () {
    return view('contents.form_training');
});
Route::get('/training','TweetController@showTraining');
Route::post('/training/store','TweetController@storeTraining');
Route::get('/training/add/{id}/{type}','TweetController@storeTrainingOne');
Route::get('/tentang-aplikasi', function(){
	return view('contents.tentang_aplikasi');
});
Route::get('/training/clear/{type}','TweetController@clear');


Route::get('/test','TweetController@test');
Route::get('/postagger','KlasifikasiController@posTagger');

Route::get('/postagging', 'postaggingController@index');



