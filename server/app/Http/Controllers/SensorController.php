<?php namespace App\Http\Controllers;
 
use App\Http\Controllers\Controller;
use App\Sensor;
use Auth;
use Request;
 
class SensorController extends Controller {
 
   /**
    * Display a listing of the resource.
    *
    * @return Response
    */
   public function index() {
 
         $sensors = Sensor::orderBy('created_at', 'desc')
		 ->take(20)->get();
	 return $sensors;
   }
 
   /**
    * Store a newly created resource in storage.
    *
    * @return Response
    */
   public function store() {
      $sensor = new Sensor(Request::all());
      $sensor->save();
      return $sensor;
   }
 
 
}