package com.example.clase9webservice.controller;

import com.example.clase9webservice.entity.Product;
import com.example.clase9webservice.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
/*si tiene un controlador que solo dará servicios webs se puede anotar con RestController, hace que todos los metodos
sean @ResponseBody*/
//@Controller

@RestController
@CrossOrigin  //para hacer solicitudes de otros IP (como desde la ip de la nube)
public class ProductoController {
    final ProductRepository productRepository;

    public ProductoController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    //devolvera en formato json una lista de los productos encontrados
    @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE+"; charset=utf-8") //evitar problem de caracteres
    public List<Product> listarProducto (){
        return productRepository.findAll();
    }

    //Obtener producto
    @GetMapping("/product/{id}")
    public ResponseEntity<HashMap<String, Object>> ObtenerProductoPorId(@PathVariable("id") String idStr){
        HashMap<String, Object> responseJson= new HashMap<>();

        try {
            int id = Integer.parseInt(idStr);
            Optional<Product> optProducto = productRepository.findById(id);
            if(optProducto.isPresent()){
                Product product = optProducto.get();
                responseJson.put("result","success");
                responseJson.put("product", product);
                return ResponseEntity.ok(responseJson);   //Para codido HTTP 200
            }else {
                responseJson.put("msg","Producto no encontrado");
            }
        }catch (NumberFormatException e){
            responseJson.put("msg","El ID debe ser un número entero positivo");
        }
        responseJson.put("result", "failure");                  //Para las respuestas erroneas se envia en mensaje de error
        return ResponseEntity.badRequest().body(responseJson);  //con un dato no valido se observara el codigo 400 badRequest
    }

    //Crear Producto    |   Enviar como respuesta el codigo HHTP 201 Created
    @PostMapping("/product")        //Para mapear el contenido de de un form por POST en JSON se usa @RequestBody
    public ResponseEntity<HashMap<String,Object>> guardarProducto(@RequestBody Product product,
                                                                  @RequestParam(value = "fetchId",required = false) boolean fetchId){
        HashMap<String,Object> responseMap = new HashMap<>();
        productRepository.save(product);
        if(fetchId){
            responseMap.put("id",product.getId());
        }
        responseMap.put("Estado","creado");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);  //Para docigo HTTP 201
    }

    //Actualizar el Producto todos sus campos   |   Para actuizar solo algunos campos se valida cada campo
    @PutMapping("/product")
    public ResponseEntity<HashMap<String,Object>> actualizarProducto(@RequestBody Product product){
        HashMap<String,Object> responseMap = new HashMap<>();

        if(product.getId() != null && product.getId() > 0){
            Optional<Product> opt = productRepository.findById(product.getId());

            if(opt.isPresent()){
                productRepository.save(product);
                responseMap.put("Estado", "actualizado");
                return ResponseEntity.ok(responseMap);    //para codigo HTTP 200
            }else {
                responseMap.put("Estado","error");
                responseMap.put("msg","El producto a actualizar no existe");
                return ResponseEntity.badRequest().body(responseMap);  //Para codigo error 400
            }
        }else {
            responseMap.put("Estado","error");
            responseMap.put("msg","Debe ingresar un ID");
            return ResponseEntity.badRequest().body(responseMap);  //Error HTTP 400
        }
    }

    //Borrar Producto
    @DeleteMapping("/product/{id}")
    public ResponseEntity<HashMap<String,Object>> borrarProducto(@PathVariable("id") String idStr){
        HashMap<String,Object> responseMap = new HashMap<>();

        try {
            int id = Integer.parseInt(idStr);
            Optional<Product> optProducto = productRepository.findById(id);
           //solo retorna si existe o no el ID, No devuelve el producto
            if( productRepository.existsById(id)){
                productRepository.deleteById(id);
                responseMap.put("Estado","borrado exitoso");
                return ResponseEntity.ok(responseMap);
            }else {
                responseMap.put("Estado","error");
                responseMap.put("msg","no se encontro el ID "+id);
                return ResponseEntity.badRequest().body(responseMap);  //error 400
            }

        }catch (NumberFormatException ex){
            responseMap.put("Estado","error");
            responseMap.put("msg","El ID debe ser un numero");
            return ResponseEntity.badRequest().body(responseMap);  //error 400
        }

    }





    //Gestionar excepcion cuando el no se mapea el JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, Object>> gestionExcepcion(HttpServletRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST") || request.getMethod().equals("PUT")){
            responseMap.put("Estado","error");
            responseMap.put("msg","Debe enviar un producto");
        }
        return ResponseEntity.badRequest().body(responseMap);   //enviar el error 400

    }
}
