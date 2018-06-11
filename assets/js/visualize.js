var container;
var camera, controls, scene, renderer, rtTexture;
var raycaster, mouse;
var visRunning = false;

var layers = [];
var colors = []; // array of THREE.Colors
var materials = [];
var edgeMesh;

var containerID = "visualizer";
var projectName = "dummyTest";
var metaColorFilename = assetPath + projectName + "_meta-colors.three.txt";
var layerFilename = assetPath + projectName + "_layers.three.txt";
var edgeFilename = assetPath + projectName + "_edges.three.txt";
var noodleFilename = assetPath + projectName + "_noodles.three.txt";

// TODO probably shouldn't be such a magic number
var zMult = 10;

function init(){
    visRunning = true;

    container = document.getElementById( containerID );

    //Camera
    camera = new THREE.PerspectiveCamera(60, 1024 / 512, 1, 1000000);
    camera.position.x = 10000;
    //camera.position.y = 0;
    camera.position.z = 500;
    camera.up = new THREE.Vector3( 0, 0, -1 ); // so orbitcontrols works with z axis - direction of our layers

    //Controls
    controls = new THREE.OrbitControls( camera, container );
    controls.addEventListener( 'change', render );

    //Scene
    scene = new THREE.Scene();

    //Lights
    var light = new THREE.AmbientLight(0xffffff, 0.5);
    scene.add(light);

    //Mouse selection
    raycaster = new THREE.Raycaster();
    mouse = new THREE.Vector2();
    rtTexture = new THREE.WebGLRenderTarget( container.innerWidth, container.innerHeight,
        { minFilter: THREE.LinearFilter, magFilter: THREE.NearestFilter, format: THREE.RGBAFormat, type: THREE.FloatType } );
    container.addEventListener( 'mousedown', onDocumentMouseDown, false );

    //Debugging / metadata
    var layercount = 0;
    var colorcount = 0;

    loadMetaColors().then(
        loadLayers
    ).then(
        loadEdges
    ).then(
        loadNoodles
    ).then(
        render
    );

    //Renderer
    renderer = new THREE.WebGLRenderer();
    renderer.setClearColor( 0x000000 );
    renderer.setPixelRatio( window.devicePixelRatio );
    renderer.setSize( $(container).width(), $(container).height(), false );

    container.appendChild( renderer.domElement );

    container.addEventListener( 'resize', onWindowResize, false );
    render();
}

function updateLabelStatus() {
    var camZ = camera.position.z;
    layers.forEach(function (layer) {
        var diff = camZ - layer.z;
        if(diff < 2000 && diff > -2000) { // if it is within the arbitrary bound
            if(layer.r != 1) { // do we need to render differently?
                layer.r = 1;
                layer.labels.forEach(function (label) {
                    label.visible = true;
                });
            }
        } else if(layer.r == 1) {
            layer.r = 0;
            layer.labels.forEach(function (label) {
                label.visible = false;
            });
        }
    });
    render();
}

function updateLayerStatus() {
    var camZ = camera.position.z;
    layers.forEach(function (layer) {
        var diff = camZ - layer.z;
        if(diff < 2000 && diff > -2000) { // if it is within the arbitrary bound
            if(layer.r != 1) { // do we need to render differently?
                layer.r = 1;
                layer.clusters.forEach(function (cluster) {
                    cluster.mesh.visible = false;
                    cluster.nodes.forEach(function (node) {
                        node.visible = true;
                    });
                });
            }
        } else if(layer.r == 1) {
            layer.r = 0;
            layer.clusters.forEach(function (cluster) {
                cluster.nodes.forEach(function (node) {
                    node.visible = false;
                });
                cluster.mesh.visible = true;
            });
        }
    });
    render();
}

function onDocumentMouseDown( event ) {
    event.preventDefault();

    mouse.x = ( event.clientX / renderer.domElement.clientWidth ) * 2 - 1;
    mouse.y = - ( event.clientY / renderer.domElement.clientHeight ) * 2 + 1;

    raycaster.setFromCamera( mouse, camera );
    //var intersects = raycaster.intersectObjects( objects );

    render();
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize( window.innerWidth, window.innerHeight );

    render();
}

function render() {
    renderer.render( scene, camera );
}

function closeScene() {
    visRunning = false;
    container.removeChild( renderer.domElement );
    camera = null;
    scene = null;
    controls.dispose();
    controls = null;
    renderer.dispose();
    renderer = null;
}

// VISIBILITY TOGGLE FUNCTIONS

function toggleLabels( visibility ) {
    console.log( 'Setting Labels to visible='+visibility );
    for( var l=0; l<layers.length; l++ ) {
        layers[l].label.visible = visibility;
        for( var c=0; c<layers[l].clusterLabels.length; c++ )
            layers[l].clusterLabels[c].visible = visibility;
        // TODO toggle node label visibility
    }
    if( visRunning )
        render();
}

function toggleEdges( visibility ) {
    console.log( 'Setting Edges to visible='+visibility );
    if( visRunning ) {
        edgeMesh.visible = visibility;
        render();
    }
}

function toggleNodes( visibility ) {
    console.log( 'Setting Nodes to visible='+visibility );
    for( var l=0; l<layers.length; l++ ) {
        layers[l].mesh.visible = visibility;
        // TODO toggle node object visibility
    }
    if( visRunning )
        render();
}

function toggleNoodles( visibility ) {
    console.log( 'Setting Noodles to visible='+visibility );
    for( var l=0; l<layers.length; l++ ) {
        for( var n=0; n<layers[l].noodles.length; n++ )
            layers[l].noodles[n].mesh.visible = visibility;
    }
    if( visRunning )
        render();
}

// FILE LOADING FUNCTIONS

function loadMetaColors() {
    return new Promise(function(resolve) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            if ( this.readyState == 4 && this.status == 200 ) {
                var text = request.responseText;
                var lines = text.split( "\n" );
                console.log( "Loading file:"+metaColorFilename );
                console.log( "saw the file: "+text );
                layercount = lines[0].split( " " )[1];
                colorcount = lines[1].split( " " )[1];
                console.log( "Layer Count:"+layercount );
                console.log( "Color Count:"+colorcount );
                for( var i=2; i<lines.length; i++ ) {
                    var line = lines[i].split( " " );
                    colors.push( new THREE.Color(
                        parseFloat( line[0] ),
                        parseFloat( line[1] ),
                        parseFloat( line[2] )
                    ));
                }
                console.log( "Finished loading file:"+metaColorFilename );
                resolve();
            }
        }
        request.open( 'GET', 'partiview_generator/get_colors', true );
        request.setRequestHeader("Cache-Control", "no-cache");
        request.send();
    });
}

function loadLayers() {
    return new Promise(function(resolve) {
        // -- PROJECTNAME_layers.three.txt --
        // layer [layerIndex] [layerZ]
        // cluster [clusterIndex] [clusterName] [cluster X,Y] [colorIndex]
        // [conceptIndex(w/in layer)] [conceptName] [conceptRadius] [concept X,Y]
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            if ( this.readyState == 4 && this.status == 200 ) {
                var text = request.responseText;
                var lines = text.split( "\n" );
                console.log( "Loading file:"+layerFilename );
                var layer = {};
                layer.init = false;
                var thisClusterIndex;

                // this has been locking up the browser because of it's long load time
                // the setTimeout and wrapper function are intended to set small break
                // points withing the loading, to stop it from totally freezing the browser
                var i = 0;
                var loadSome = function () {
                    for( ; i<lines.length; i++ ) {
                        line = lines[i].split( " " );
                        if( line[0] == "layer" ) {
                            if( layer.init ) { // basically just a flag to not trip the first time thru
                                layer.mesh = new THREE.Mesh( layer.geometry, layer.mats );
                                scene.add(layer.mesh);
                                layers.push( layer );
                            }
                            layer = {};
                            layer.init = true;
                            layer.index = parseInt( line[1] );
                            layer.noodles = [];
                            layer.clusters = [];
                            layer.nodes = [];
                            layer.nodeLabels = [];
                            layer.clusterLabels = [];
                            layer.z = parseFloat( line[2] )*100*zMult;
                            layer.r = 0; // used to indicate what state the layer is in - are labels loaded, etc
                            layer.geometry = new THREE.Geometry();
                            layer.mesh = null;
                            layer.mats = [];
                            layer.labelMats = [];

                            // TODO create layer label - name / date
                            var textSprite = new THREE.TextSprite({
                                textSize: 500,
                                redrawInterval: 1,
                                material: { color: new THREE.Color( 0xffffff ) },
                                texture: {
                                    text: 'Layer '+layer.index+' z:'+layer.z,
                                    fontFamily: 'Arial, Helvetica, sans-serif',
                                    autoRedraw: true
                                }
                            });
                            textSprite.position.set( 0, 0, layer.z );
                            textSprite.visible = true;
                            layer.label = textSprite;
                            scene.add( layer.label );
                        }
                        else if( line[0] == "cluster" ) {
                            var cluster = {};
                            cluster.index = parseInt( line[1] );
                            cluster.name = line[2];
                            cluster.x = parseFloat( line[3] );
                            cluster.y = parseFloat( line[4] );
                            cluster.colorIndex = parseInt( line[5] );
                            cluster.nodes = [];
                            cluster.links = []; // names of clusters in next layer it is linked to
                            cluster.allTextShown = false; // are the individual concept labels shown?
                            layer.mats.push( new THREE.MeshLambertMaterial({
                                color: colors[cluster.colorIndex],
                                transparent: true,
                                opacity: 0.7
                            }));
                            layer.labelMats.push( {
                                color: colors[cluster.colorIndex]
                            })
                            var textSprite = new THREE.TextSprite({
                                textSize: 200, // TODO needs to be set from max radius or something
                                redrawInterval: 1,
                                material: layer.labelMats[cluster.index],
                                texture: {
                                    text: cluster.name,
                                    fontFamily: 'Arial, Helvetica, sans-serif',
                                    autoRedraw: true
                                }
                            });
                            textSprite.position.set( cluster.x, cluster.y, layer.z-100 );
                            textSprite.visible = true;
                            cluster.textSprite = textSprite;
                            layer.clusterLabels.push( cluster.textSprite );
                            scene.add( cluster.textSprite );
                            thisClusterIndex = cluster.index;
                            layer.clusters.push( cluster );
                        }
                        else {
                            // [conceptIndex(w/in layer)] [conceptName] [conceptRadius] [concept X,Y]
                            var node = {};
                            node.index = parseInt( line[0] );
                            node.name = line[1];
                            node.radius = parseFloat( line[2] );
                            node.x = parseFloat( line[3] )+layer.clusters[thisClusterIndex].x;
                            node.y = parseFloat( line[4] )+layer.clusters[thisClusterIndex].y;

                            var geometry = new THREE.SphereGeometry( node.radius );
                            var material = layer.mats[thisClusterIndex];
                            var mesh = new THREE.Mesh(geometry, material);
                            mesh.position.x = node.x;
                            mesh.position.y = node.y;
                            mesh.position.z = layer.z;
                            mesh.updateMatrix();
                            mesh.matrixAutoUpdate = false;
                            node.mesh = mesh;

                            layer.nodes.push( node );
                            layer.geometry.merge(mesh.geometry, mesh.matrix, thisClusterIndex);

                            var textSprite = new THREE.TextSprite({
                                textSize: 10, // TODO needs to be set from max radius or something
                                redrawInterval: 1,
                                material: layer.labelMats[thisClusterIndex],
                                texture: {
                                    text: node.name,
                                    fontFamily: 'Arial, Helvetica, sans-serif',
                                    autoRedraw: true
                                }
                            });
                            textSprite.position.set( node.x, node.y, layer.z-20 );
                            textSprite.visile = true;
                            layer.nodeLabels.push( textSprite );

                            node.textSprite = textSprite;
                            layer.clusters[thisClusterIndex].nodes.push( node );

                            if( i == lines.length-1 ) {
                                layer.mesh = new THREE.Mesh( layer.geometry, layer.mats );
                                scene.add(layer.mesh);
                                layers.push( layer );
                            }
                        }
                        if( i + 1 == lines.length ) {
                            console.log( "Finished loading file:"+layerFilename );
                            resolve();
                        }
                        if ( i + 1 < lines.length && i % 50 == 0) {
                            //console.log('Pausing for a bit...'+i);
                            setTimeout(loadSome, 5);
                            i++;
                            break;
                        }
                    }
                };
                loadSome();
            }
        }
        request.open( 'GET', 'partiview_generator/get_layers', true );
        request.setRequestHeader("Cache-Control", "no-cache");
        request.send();
    });
}

function loadEdges() {
    return new Promise(function(resolve) {
        // -- PROJECTNAME_edges.three.txt --
        // edges
        // layer [layerIndex]
        // [nodeIndex] [nodeIndex]
        // [nodeIndex] [nodeIndex]
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            if( this.readyState == 4 && this.status == 200 ) {
                console.log( "Loading file:"+edgeFilename );
                var text = request.responseText;
                var lines = text.split( "\n" );
                var edgeGeom = new THREE.Geometry();
                var edgemat = new THREE.LineBasicMaterial({
                  color: 0x0000ff,
                  transparent: true,
                  opacity: 0.1
                });
                var layerIndex = 0;
                for( var i=1; i<lines.length; i++ ) {
                    var line = lines[i].split( " " );
                    if( line[0] == "layer" ) {
                        layerIndex = parseInt( line[1] );
                    }
                    else if( line[0] != "" ) {
                        var startNode = layers[layerIndex].nodes[parseInt( line[0] )];
                        var endNode = layers[layerIndex].nodes[parseInt( line[1] )];
                        var startVector = new THREE.Vector3( startNode.x, startNode.y, layers[layerIndex].z );
                        var endVector = new THREE.Vector3( endNode.x, endNode.y, layers[layerIndex].z );
                        edgeGeom.vertices.push( startVector, endVector );
                    }
                }
                var allEdges = new THREE.LineSegments( edgeGeom, edgemat );
                scene.add( allEdges );
                edgeMesh = allEdges;
                console.log( "Finished loading file:"+edgeFilename );
                resolve();
            }
        }
        request.open( 'GET', 'partiview_generator/get_edges', true );
        request.setRequestHeader("Cache-Control", "no-cache");
        request.send();
    });
}

var noodleMat =  new THREE.MeshLambertMaterial({ // TEMP
    color: 0x005f5f,
    transparent: true,
    opacity: 0.7
});

function loadNoodles() {
    return new Promise(function(resolve) {
        // -- PROJECTNAME_noodles.three.txt --
        // noodles
        // [layerIndex0] [clusterName0] [layerIndex1] [clusterName1]
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            if( this.readyState == 4 && this.status == 200 ) {
                console.log( "Loading file:"+noodleFilename );
                var text = request.responseText;
                var lines = text.split( "\n" );

                for( var i=1; i<lines.length; i++ ) {
                    if( lines[i] == "" )
                        continue;
                    var line = lines[i].split( " " );
                    var startLayerIndex = parseInt( line[0] );
                    var startConceptName = line[1];
                    var endLayerIndex = parseInt( line[2] );
                    var endConceptName = line[3];

                    // find the start and end clusters by conceptName
                    var noodle = {};
                    var startLayer = layers[startLayerIndex];
                    for( var j=0; j<startLayer.clusters.length; j++ ) {
                        for( var n=0; n<startLayer.clusters[j].nodes.length; n++ ) {
                            if( startLayer.clusters[j].nodes[n].name == startConceptName ) {
                                noodle.startConcept = startLayer.clusters[j].nodes[n];
                                noodle.startCluster = startLayer.clusters[j];
                            }
                        }
                    }
                    var endLayer = layers[endLayerIndex];
                    for( var j=0; j<endLayer.clusters.length; j++ ) {
                        for( var n=0; n<endLayer.clusters[j].nodes.length; n++ ) {
                            if( endLayer.clusters[j].nodes[n].name == endConceptName ) {
                                noodle.endConcept = endLayer.clusters[j].nodes[n];
                                noodle.endCluster = endLayer.clusters[j];
                            }
                        }
                    }

                    // make the curve and geometry
                    noodle.top = new THREE.Vector3( noodle.startCluster.x, noodle.startCluster.y, startLayer.z );
                    noodle.bot = new THREE.Vector3( noodle.endCluster.x, noodle.endCluster.y, endLayer.z );
                    noodle.mid = new THREE.Vector3( (noodle.top.x+noodle.bot.x)/2,
                                                    (noodle.top.y+noodle.bot.y)/2,
                                                    (noodle.top.z+noodle.bot.z)/2 );
                    var curve = new THREE.QuadraticBezierCurve3( noodle.top, noodle.mid, noodle.bot );
                    var geom = new THREE.TubeGeometry( curve, 10, 10 );
                    noodle.mesh = new THREE.Mesh( geom, noodleMat );

                    // is the start cluster already linked to the end cluster?
                    var linkAlreadyExists = false;
                    for( var l=0; l<noodle.startCluster.links.length; l++ ) {
                        if( noodle.startCluster.links[i] == noodle.endCluster.name )
                            linkAlreadyExists = true;
                    }

                    // if the link doesn't exist, add it to the layer, the scene,
                    // and to the startCluster's links
                    if( !linkAlreadyExists ) {
                        startLayer.noodles.push( noodle );
                        scene.add( noodle.mesh );
                        noodle.startCluster.links.push( noodle.endCluster.name );
                    }
                }
                hideLoadingWheel();
                console.log( "Finished loading file:"+noodleFilename );
                resolve();
            }
        }
        request.open( 'GET', 'partiview_generator/get_noodles', true );
        request.setRequestHeader("Cache-Control", "no-cache");
        request.send();
    });
}
