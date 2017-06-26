// Nicholas Espinosa
// PID n2431401
// COP 3503 - 0001
// Recitation 7.5
// Eight Queens Assignment

package main;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.imageio.ImageIO;

// TODO
/*	Reference other projects to get to final result
 *  Image has loaded, just need to add in the automation via button
 */

public class EightQueens extends Applet implements MouseListener, MouseMotionListener, Runnable, ActionListener
{
	static final int NUMROWS = 8;
	static final int NUMCOLS = 8;
	static final int SQUAREWIDTH = 50;
	static final int SQUAREHEIGHT = 50;
	static final int BOARDLEFT = 50;
	static final int BOARDTOP = 50;
	
	int m_nBoard[][] = new int[8][8];
	boolean m_bClash = false;
	String m_strStatus = "";
	Button m_Button;
	Thread m_Thread;
	
	Image m_imgQueen;
	MediaTracker tracker = new MediaTracker(this);
	boolean m_bSolveIt;
	
	public void init()
	{
		// Setting the size
		setSize(1020, 700);
        
        // Adding the button
        m_Button = new Button("Solve It!");
        m_Button.addActionListener(this);
        this.add(m_Button);
		
		try
		{
			// Loading the imagin onto the MediaTracker
			m_imgQueen = ImageIO.read(EightQueens.class.getResourceAsStream("queen.png"));
			tracker.addImage(m_imgQueen, 1);
			tracker.waitForAll();
		}
		catch(Exception e)
		{

		}
		
		// New thread is started
		m_Thread = new Thread();
		m_Thread.start();
	}
	
	// Solve it method
	public boolean SolveIt(int col)
	{
		// If columns are past the bounds
		if(col >= 8)
			return true;
		
		// For each row
		for(int row = 0; row < 8; row++)
		{
			// Modify the point and repaint
			m_nBoard[row][col] = 1;
			//repaint();
			this.paint(getGraphics());
			Delay(10);
			
			// if there's no clash
			if(!m_bClash)
			{
				// If next column returns true
				if(SolveIt(col + 1))
				{
					// return true if the next is true
					return true;
				}
			}
			
			// Otherwise, modify the position back
			m_nBoard[row][col] = 0;
			//repaint();
			this.paint(getGraphics());
			Delay(10);
		}
		
		// Not possible at the moment
		return false;
	}
	
	public void Delay(int milli)
	{
		// Delays the function
		try
		{
			Thread.sleep(milli);
		}
		catch(Exception ex)
		{
			
		}
	}

	public void paint (Graphics canvas)
	{	
		// Clears the board
		canvas.clearRect(0, 0, 1020, 700);
		
		// Proceeds with the paint
		m_bClash = false;
		DrawSquares( canvas );
		canvas.setColor(Color.RED);
		CheckColumns( canvas );
		CheckRows( canvas );
		CheckDiagonal1( canvas );
		CheckDiagonal2( canvas );
		canvas.setColor(Color.BLUE);
		canvas.drawString(m_strStatus, 
		BOARDLEFT, BOARDTOP + SQUAREHEIGHT * 8 + 20);
	}
	void DrawSquares( Graphics canvas )
	{
		canvas.setColor(Color.BLACK);
		for( int nRow=0; nRow<NUMROWS; nRow++ )
		{
			for( int nCol=0; nCol<NUMCOLS; nCol++ )
			{
				canvas.drawRect( BOARDLEFT + nCol * SQUAREWIDTH,
					BOARDTOP + nRow * SQUAREHEIGHT, SQUAREWIDTH, SQUAREHEIGHT );
					
				if( m_nBoard[nRow][nCol] != 0 )
				{
					canvas.drawImage( m_imgQueen,
						BOARDLEFT + nCol * SQUAREWIDTH + 8, BOARDTOP + nRow * SQUAREHEIGHT + 6, null );
				}
			}
		}
	}
		
	void CheckColumns( Graphics canvas )
	{
		// Check all columns
		for(  int nCol=0; nCol<NUMCOLS; nCol++ )
		{
			int nColCount = 0;
			for( int nRow=0; nRow<NUMROWS; nRow++ )
			{
				if( m_nBoard[nRow][nCol] != 0 )
				{
					nColCount++;
				}
			}
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
					BOARDTOP + ( SQUAREHEIGHT / 2 ),	
					BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
					BOARDTOP + SQUAREHEIGHT * 7 + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}
	}

	void CheckRows( Graphics canvas )
	{
		for(  int nRow=0; nRow<NUMROWS; nRow++ )
		{
			int nRowCount = 0;
			for( int nCol=0; nCol<NUMCOLS; nCol++ )
			{
				if( m_nBoard[nRow][nCol] != 0 )
				{
					nRowCount++;
				}
			}
			if( nRowCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + ( SQUAREWIDTH / 2 ),
					BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
					BOARDLEFT + 7 * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
					BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}
	}
		
	void CheckDiagonal1( Graphics canvas )
	{
		// Check diagonal 1
		
		for( int nRow=NUMROWS-1; nRow>=0; nRow-- )
		{
			int nCol = 0;
				
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol < NUMCOLS &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol++;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol - 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}

		for( int nCol=1; nCol<NUMCOLS; nCol++)
		{
			int nRow = 0;
			
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol < NUMCOLS &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol++;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol - 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}
	}
		
	void CheckDiagonal2( Graphics canvas )
	{
		// Check diagonal 2
			
		for( int nRow=NUMROWS-1; nRow>=0; nRow-- )
		{
			int nCol = NUMCOLS - 1;
				
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol >= 0 &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol--;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol + 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}

		for( int nCol=NUMCOLS-1; nCol>=0; nCol--)
		{
			int nRow = 0;
			
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol >= 0 &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol--;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol + 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
				
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		// Set solve it to true
		m_bSolveIt = true;
		run();
	}

	@Override
	public void run()
	{
		
			// If solve it is true, perform the function
			if(m_bSolveIt)
			{
				SolveIt(0);
				m_bSolveIt = false;
			}
				
			// Then delay
			Delay(10);
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
