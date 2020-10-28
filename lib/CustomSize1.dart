import 'package:flutter/material.dart';

class CustomSize1 extends StatelessWidget {
  CustomSize1({
    Key key,
  }) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xffffffff),
      body: Stack(
        children: <Widget>[
          Transform.translate(
            offset: Offset(97.0, 97.0),
            child: Container(
              width: 318.0,
              height: 318.0,
              decoration: BoxDecoration(
                borderRadius:
                    BorderRadius.all(Radius.elliptical(9999.0, 9999.0)),
                gradient: LinearGradient(
                  begin: Alignment(0.0, -1.0),
                  end: Alignment(0.0, 1.0),
                  colors: [
                    const Color(0xffffffff),
                    const Color(0xff57d7d9),
                    const Color(0xff252c65)
                  ],
                  stops: [0.0, 1.0, 1.0],
                ),
                border: Border.all(width: 3.0, color: const Color(0xff30c1c4)),
              ),
            ),
          ),
          Transform.translate(
            offset: Offset(250.0, 93.0),
            child:
                // Adobe XD layer: 'PinClipart.com_syri…' (shape)
                Container(
              width: 165.0,
              height: 152.0,
              decoration: BoxDecoration(
                image: DecorationImage(
                  image: const AssetImage(''),
                  fit: BoxFit.fill,
                ),
              ),
            ),
          ),
          Transform.translate(
            offset: Offset(155.0, 122.0),
            child: Transform.rotate(
              angle: -0.2967,
              child: SizedBox(
                width: 341.0,
                height: 278.0,
                child: Text(
                  'Covid',
                  style: TextStyle(
                    fontFamily: 'Marker Felt',
                    fontSize: 50,
                    color: const Color(0xffffffff),
                  ),
                  textAlign: TextAlign.left,
                ),
              ),
            ),
          ),
          Transform.translate(
            offset: Offset(122.0, 225.0),
            child: Text(
              'Tracker',
              style: TextStyle(
                fontFamily: 'Marker Felt',
                fontSize: 80,
                color: const Color(0xffffffff),
              ),
              textAlign: TextAlign.left,
            ),
          ),
        ],
      ),
    );
  }
}
