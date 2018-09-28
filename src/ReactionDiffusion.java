package ReactionDiffusion;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;



class ReactionDiffusion extends Frame{
	public static void main( String argv[] ){
		ReactionDiffusion rd = new ReactionDiffusion();
	}
	ReactionDiffusion(){
		ReactionDiffusionCanvas rdc = new ReactionDiffusionCanvas();
		add( rdc );
		pack();
		setVisible( true );
	}
}

class ReactionDiffusionCanvas extends Canvas{

  double ang_f = 0.4;
  double minlen_f = 10;
  double maxlen_f = 20;
  double bif_f = 0.9;
  int cnt_f = 300;

	int width = 600, height = 600;
	BufferedImage off = new BufferedImage( width, height, java.awt.image.BufferedImage.SCALE_SMOOTH);
		//bimg = new java.awt.image.BufferedImage(width, height,img.SCALE_SMOOTH);
	BufferedImage bimg = null;
  double[] u = new double[width*height];

	public int[] int2rgb( int p ){
		int[] rgb = new int[3];
		rgb[0] = (p >> 16) & 0xff;
		rgb[1] = (p >> 8 ) & 0xff;
		rgb[2] = (p      ) & 0xff;
		return rgb;
	}


	public int rgb2int( int r, int g, int b ){
		if ( r > 255 ) r = 255;
		if ( g > 255 ) g = 255;
		if ( b > 255 ) b = 255;
		if ( r < 0 ) r = 0;
		if ( g < 0 ) g = 0;
		if ( b < 0 ) b = 0;
		return (0xff000000 | r << 16 | g << 8 | b );
	}


	ReactionDiffusionCanvas(){
		setSize( width, height );
		setVisible( true );
	}

  public void diff(){
			for ( int y = 0; y < height; y++ ){
				for ( int x = 0; x < width; x++ ){
          if ( x > 0 && y > 0 && x < width - 1 && y < height - 1 ){
            double top     = u[ (y-1) * width + x ];
            double left    = u[ y * width + x-1 ];
            double right   = u[ y * width + x+1 ];
            double bottom  = u[ (y+1) * width + x ];
            double i = u[ y * width + x ];
            double ni = ( left + top + bottom + right ) / 4.0;
            if ( ni > i ) u[ y * width + x ] = ni;
          }
        }
      }
  }

	public void update(Graphics g){ paint(g); }
	public void paint(Graphics g){

      Image img = null;


		Graphics2D og = (Graphics2D)off.getGraphics();
    BasicStroke BStroke = new BasicStroke(2.0f);
    og.setStroke(BStroke);


    for ( int i = 0; i < 100; i++ ){
        og.setColor( Color.black );
        og.fillRect( 0, 0, width, height);
        tick( 0, og, width/2, 0 );


        g.drawImage( off , 0, 0, this);



      try{
          OutputStream out=new FileOutputStream("imgs\\thunder_"+i+".jpg");
          ImageIO.write(off, "jpg", out);
        }catch(IOException e){
          e.printStackTrace();
        }


       ang_f = Math.random()*0.95;
       minlen_f = 50 * Math.random();
       maxlen_f = minlen_f + Math.random() * 150;
       bif_f = 0.5 + Math.random() * 0.45;
       cnt_f = (int)(200 * Math.random());
    }

    System.exit(0);

  }

  public void tick(int cnt, Graphics2D g, int x, int y ){
    double ang = ( 180 * ang_f ) * Math.random() * Math.PI / 180.0;
    double len = minlen_f + ( maxlen_f - minlen_f ) * Math.random();
    double th = Math.PI/2.0 - ang / 2.0 + Math.random() * ang;

    int py = (int)(len * Math.sin( th ) + y);
    int px = (int)(len * Math.cos( th ) + x);

    g.setColor( new Color( (float)(0.9 + Math.random() * 0.1), (float)(0.9 + Math.random() * 0.1),
                          (float)(Math.random() * 0.05), (float)(0.5 + Math.random() * 0.5) ) );
    g.drawLine( x,y, (int)px, (int)py);

    cnt++;
    if ( cnt > cnt_f * Math.random() ) return;
    if ( y > height*2 ) return;

    if ( Math.random() > bif_f ){
      tick( cnt, g, px, py );
      tick( cnt, g, px, py );
    }else{
      tick( cnt, g, px, py );
    }
  }

}
