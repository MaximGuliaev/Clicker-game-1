import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Maxcim
 *
 */
public class GameWindow extends JFrame {
	private static GameWindow game_window;
	private static long last_frame_time;
	private static Image background;

	private static Image start;
	private static float start_left = 300;
	private static float start_top = 180;

	private static Image play_again;
	private static Image garbage;
	private static float garbage_left = 200;
	private static float garbage_top = -100;
	private static float garbage_v = 200;
	private static int score;

	public static void main(String[] args) throws Exception {
		background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
		play_again = ImageIO.read(GameWindow.class.getResourceAsStream("play_again.png"));
		garbage = ImageIO.read(GameWindow.class.getResourceAsStream("garbage.png"));
		start = ImageIO.read(GameWindow.class.getResourceAsStream("start.png"));

		game_window = new GameWindow();
		game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game_window.setLocation(200, 100);
		game_window.setSize(1000, 650);
		game_window.setResizable(false);

		GameField game_field = new GameField();

		game_field.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();

				float start_right = start_left + start.getWidth(null);
				float start_bottom = start_top + start.getHeight(null);
				boolean is_start = x >= start_left && x <= start_right && y >= start_top && y <= start_bottom;
				if (is_start) {
					start_top = 1300;
					last_frame_time = System.nanoTime();
				}
				float drop_right = garbage_left + garbage.getWidth(null);
				float drop_bottom = garbage_top + garbage.getHeight(null);
				boolean is_drop = x >= garbage_left && x <= drop_right && y >= garbage_top && y <= drop_bottom;
				if (is_drop) {
					garbage_top = -100;
					garbage_left = (int) (Math.random() * (game_field.getWidth() - garbage.getWidth(null)));

					garbage_v = garbage_v + 20;
					score++;
					game_window.setTitle("Score: " + score);
				}
			}
		});
		game_window.add(game_field);
		game_window.setVisible(true);
	}

	private static void onRepaint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.drawImage(start, (int) start_left, (int) start_top, null);

		if (start_top > game_window.getHeight()) {
			long current_time = System.nanoTime();
			float delta_time = (current_time - last_frame_time) * 0.000000001f;
			last_frame_time = current_time;

			garbage_top = garbage_top + garbage_v * delta_time;

			g.drawImage(garbage, (int) garbage_left, (int) garbage_top, null);
			if (garbage_top > game_window.getHeight()) {
				g.drawImage(play_again, 300, 180, null);
			}
		}
	}

	private static class GameField extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			onRepaint(g);
			repaint();
		}
	}
}