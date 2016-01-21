<?php namespace App\Http\Controllers;
 
use App\Http\Controllers\Controller;
use App\Sensor;
use Auth;
use Illuminate\Http\Request;
 
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
   public function store(Request $request) {
      $sensor = new Sensor;
	  $sensor->sensor = $request->sensor;
	  $sensor->value = $request->value;
	  $sensor->latitude = $request->latitude;
	  $sensor->longitude = $request->longitude;
      $sensor->save();
      return $sensor;
   }
 
    /**
    * Store a newly created resource in storage.
    *
    * @return Response
    */
   public function deleteall() {      
      Sensor::truncate();
	  return [];
   }
 
}