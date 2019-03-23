public class Projectile {

	private int rad, range;
	private double vel, traj, x, , ox, oy;
	private String type;

	public Projectile(double xin, double yin, double vin, double trin, Stirng tyin, double ranin, double radin) {
		x = xin;
		y = yin;
		ox = 99999;
		oy = 99999;

		vel = vin;
		traj = trin;
		range = ranin;
		rad = radin;

		type = tyin;
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public int getIntX() { return (int)x; }
	public int getIntY() { return (int)y; }

	public void move() {
		ox = x;
		oy = y;
		x += Math.cos(traj) * vel;
		y += Math.cos(traj) * vel;
	}
}