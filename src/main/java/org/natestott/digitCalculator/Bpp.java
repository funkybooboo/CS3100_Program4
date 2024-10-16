package org.natestott.digitCalculator;

/**
 * Prints the nth number of pi in base 10.
 * This program is based on Bellard's work.
 * <p>
 * Original Author: feltocraig
 * Modified by: Nate Stott
 *
 * <p>This class implements the DigitCalculator interface and computes
 * specific decimal digits of Pi based on Bellard's formula.</p>
 */
public class Bpp implements DigitCalculator {
	/**
	 * Runs the program
	 * @param args
	 */
	public static void main(String args[]) {
		long NUM = 13;

		long duration = System.currentTimeMillis();

		Bpp bpp = new Bpp();
		System.out.println("Decimal digits of pi at position " + NUM + ": " + bpp.getDigit(NUM) + "\n");

		duration = System.currentTimeMillis() - duration;
		System.out.println("> " + duration + " ms");
	}

	/**
	 * Returns the nth digit of pi
	 * @param n - nth number of pi to return
	 * @return returns an integer value containing 8 digits after n
	 */
	public int getDigit(long n) {
		long av, a, vmax, N, num, den, k, kq, kq2, t, v, s, i;
		double sum;

		N = (long) ((n + 20) * Math.log(10) / Math.log(2));

		sum = 0;

		for (a = 3; a <= (2 * N); a = nextPrime(a)) {

			vmax = (long) (Math.log(2 * N) / Math.log(a));
			av = 1;
			for (i = 0; i < vmax; i++)
				av = av * a;

			s = 0;
			num = 1;
			den = 1;
			v = 0;
			kq = 1;
			kq2 = 1;

			for (k = 1; k <= N; k++) {

				t = k;
				if (kq >= a) {
					do {
						t = t / a;
						v--;
					} while ((t % a) == 0);
					kq = 0;
				}
				kq++;
				num = mulMod(num, t, av);

				t = (2 * k - 1);
				if (kq2 >= a) {
					if (kq2 == a) {
						do {
							t = t / a;
							v++;
						} while ((t % a) == 0);
					}
					kq2 -= a;
				}
				den = mulMod(den, t, av);
				kq2 += 2;

				if (v > 0) {
					t = modInverse(den, av);
					t = mulMod(t, num, av);
					t = mulMod(t, k, av);
					for (i = v; i < vmax; i++)
						t = mulMod(t, a, av);
					s += t;
					if (s >= av)
						s -= av;
				}

			}

			t = powMod(10, n - 1, av);
			s = mulMod(s, t, av);
			sum = (sum + (double) s / (double) av) % 1;
		}
		return getDigit((int) (sum * 1e9)); // 1e9 is 9 decimal places
	}

	private int getDigit(int digits) {
		String s = String.valueOf(digits); // Convert the integer to a string
		if (s.length() == 9) {
			char firstChar = s.charAt(0); // Get the first character
			return Character.getNumericValue(firstChar); // Convert it to an integer
		}
		return 0; // Return 0 if the length is not 9
	}

	private long mulMod(long a, long b, long m) {
		return (a * b) % m;
	}

	private long modInverse(long a, long n) {
		long i = n, v = 0, d = 1;
		while (a > 0) {
			long t = i / a, x = a;
			a = i % x;
			i = x;
			x = d;
			d = v - t * x;
			v = x;
		}
		v %= n;
		if (v < 0)
			v = (v + n) % n;
		return v;
	}

	private long powMod(long a, long b, long m) {
		long tempo;
		if (b == 0)
			tempo = 1;
		else if (b == 1)
			tempo = a;

		else {
			long temp = powMod(a, b / 2, m);
			if (b % 2 == 0)
				tempo = (temp * temp) % m;
			else
				tempo = ((temp * temp) % m) * a % m;
		}
		return tempo;
	}

	private boolean isPrime(long n) {
		if (n == 2 || n == 3)
			return true;
		if (n % 2 == 0 || n % 3 == 0 || n < 2)
			return false;

		long sqrt = (long) Math.sqrt(n) + 1;

		for (long i = 6; i <= sqrt; i += 6) {
			if (n % (i - 1) == 0)
				return false;
			else if (n % (i + 1) == 0)
				return false;
		}
		return true;
	}

	private long nextPrime(long n) {
		if (n < 2)
			return 2;
		if (n == 9223372036854775783L) {
			System.err.println("Next prime number exceeds Long.MAX_VALUE: " + Long.MAX_VALUE);
			return -1;
		}
		for (long i = n + 1;; i++)
			if (isPrime(i))
				return i;
	}
}
